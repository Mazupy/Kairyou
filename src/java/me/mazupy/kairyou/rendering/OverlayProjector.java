package me.mazupy.kairyou.rendering;

import java.util.ArrayList;
import java.util.List;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.util.math.Vec2f;

import me.mazupy.kairyou.event.Render2DEvent;
import me.mazupy.kairyou.utils.Color;

import static me.mazupy.kairyou.Kairyou.*;
import static me.mazupy.kairyou.utils.MathUtils.*;

public class OverlayProjector implements Listenable {

    /**
     * This only class only exists because I couldn't get anything to render on the worldRenderEvent and 
     * I couldn't figure out directly rendering 3D!
     * If you know why/how, please tell me, thank you in advance!
     */

    public OverlayProjector() {
        EVENT_BUS.subscribe(this);
    }

    private static float cameraYaw() {
        final float OFFSET = 180;
        final float rawYaw = MC.gameRenderer.getCamera().getYaw();
        return ((rawYaw + OFFSET) % 360 + 360) % 360 - OFFSET;
    }

    // TODO: clean up this mess when you don't have to trial and error OpenGL while figuring out projection math for over 26 hours
    // If anyone is sucessful in using simpler math / projection matrices, please do a PR

    private static List<LineRenderTask> lineRenderQueue = new ArrayList<>(10240);
    private static List<QuadRenderTask> quadRenderQueue = new ArrayList<>(10240);

    public static void line(double x0, double y0, double z0, double x1, double y1, double z1, Color color) {
        lineRenderQueue.add(new LineRenderTask(x0, y0, z0, x1, y1, z1, color));
    }

    public static void quad(double x0, double y0, double z0, double x1, double y1, double z1, double x2, double y2,
            double z2, double x3, double y3, double z3, Color color) {
        quadRenderQueue.add(new QuadRenderTask(x0, y0, z0, x1, y1, z1, x2, y2, z2, x3, y3, z3, color));
    }

    public static void box(double x, double y, double z, Color color) {
        // East (X-)
        quadRenderQueue.add(new QuadRenderTask(x, y, z, x, y, z + 1, x, y + 1, z + 1, x, y + 1, z, color));
        // West (X+)
        quadRenderQueue.add(new QuadRenderTask(x + 1, y, z, x + 1, y, z + 1, x + 1, y + 1, z + 1, x + 1, y + 1, z, color));
        // Bottom (Y-)
        quadRenderQueue.add(new QuadRenderTask(x, y, z, x + 1, y, z, x + 1, y, z + 1, x, y, z + 1, color));
        // Top (Y+)
        quadRenderQueue.add(new QuadRenderTask(x, y + 1, z, x + 1, y + 1, z, x + 1, y + 1, z + 1, x, y + 1, z + 1, color));
        // North (Z-)
        quadRenderQueue.add(new QuadRenderTask(x, y, z, x + 1, y, z, x + 1, y + 1, z, x, y + 1, z, color));
        // South (Z+)
        quadRenderQueue.add(new QuadRenderTask(x, y, z + 1, x + 1, y, z + 1, x + 1, y + 1, z + 1, x, y + 1, z + 1, color));
    }

    public static void lineBox(double x, double y, double z, float w, float h, Color color) {
        final double MIN_X = x - w / 2;
        final double MIN_Z = z - w / 2;
        final double MAX_X = x + w / 2;
        final double MAX_Z = z + w / 2;
        final double MAX_Y = y + h;

        line(MIN_X, y, MIN_Z, MAX_X, y, MIN_Z, color);
        line(MIN_X, y, MIN_Z, MIN_X, y, MAX_Z, color);
        line(MIN_X, y, MIN_Z, MIN_X, MAX_Y, MIN_Z, color);

        line(MAX_X, MAX_Y, MIN_Z, MAX_X, y, MIN_Z, color);
        line(MAX_X, MAX_Y, MIN_Z, MIN_X, MAX_Y, MIN_Z, color);
        line(MAX_X, MAX_Y, MIN_Z, MAX_X, MAX_Y, MAX_Z, color);
        
        line(MIN_X, MAX_Y, MAX_Z, MIN_X, y, MAX_Z, color);
        line(MIN_X, MAX_Y, MAX_Z, MIN_X, MAX_Y, MIN_Z, color);
        line(MIN_X, MAX_Y, MAX_Z, MAX_X, MAX_Y, MAX_Z, color);

        line(MAX_X, y, MAX_Z, MAX_X, y, MIN_Z, color);
        line(MAX_X, y, MAX_Z, MIN_X, y, MAX_Z, color);
        line(MAX_X, y, MAX_Z, MAX_X, MAX_Y, MAX_Z, color);
    }

    @EventHandler
    private final Listener<Render2DEvent> on2DRender = new Listener<>(event -> {
        for (LineRenderTask task : lineRenderQueue) {
            Vec2f screenPos0 = projection(task.pos0);
            Vec2f screenPos1 = projection(task.pos1);
            if (screenPos0 == null || screenPos1 == null) continue;

            ShapeRenderer.line(screenPos0.x, screenPos0.y, screenPos1.x, screenPos1.y, task.color);
        }
        lineRenderQueue.clear();

        for (QuadRenderTask task : quadRenderQueue) {
            Vec2f screenPos0 = projection(task.pos0);
            Vec2f screenPos1 = projection(task.pos1);
            Vec2f screenPos2 = projection(task.pos2);
            Vec2f screenPos3 = projection(task.pos3);
            if (screenPos0 == null || screenPos1 == null || screenPos2 == null || screenPos3 == null) continue;

            ShapeRenderer.quad(screenPos0.x, screenPos0.y, screenPos1.x, screenPos1.y, screenPos2.x, screenPos2.y, screenPos3.x, screenPos3.y, task.color);
        }
        quadRenderQueue.clear();
    });

    private static Vec2f projection(Vector3d pos) {
        double x = pos.x - MC.gameRenderer.getCamera().getPos().x;
        double y = pos.y - MC.gameRenderer.getCamera().getPos().y;
        double z = pos.z - MC.gameRenderer.getCamera().getPos().z;
        z *= -1; // WTF? I checked the formulas 5 times on Wikipedia and found no errors, why is this necessary?
        // https://en.wikipedia.org/wiki/3D_projection#Mathematical_formula (also where the following is derived from)
        double thetaX = -MC.gameRenderer.getCamera().getPitch() * DEG2RAD;
        double thetaY = cameraYaw() * DEG2RAD;
        double eX = ShapeRenderer.maxX() / 2;
        double eY = ShapeRenderer.maxY() / 2;
        double eZ = ShapeRenderer.maxY() / 2 * degTan(90 - MC.options.fov / 2);
        double sX = sin(thetaX);
        double sY = sin(thetaY);
        double cX = cos(thetaX);
        double cY = cos(thetaY);
        double dX = cY * x - sY * z;
        double dY = sX * (cY * z + sY * x) + cX * y;
        double dZ = cX * (cY * z + sY * x) - sX * y;
        if (dZ >= 0) return null;
        double bX = eZ / dZ * dX + eX;
        double bY = eZ / dZ * dY + eY;
        return new Vec2f((float) bX, (float) bY);
    }

    protected static class LineRenderTask {
        Color color;
        Vector3d pos0;
        Vector3d pos1;

        LineRenderTask(double x0, double y0, double z0, double x1, double y1, double z1, Color c) {
            pos0 = new Vector3d(x0, y0, z0);
            pos1 = new Vector3d(x1, y1, z1);
            color = c;
        }
    }

    protected static class QuadRenderTask {
        Color color;
        Vector3d pos0;
        Vector3d pos1;
        Vector3d pos2;
        Vector3d pos3;

        QuadRenderTask(double x0, double y0, double z0, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, Color c) {
            pos0 = new Vector3d(x0, y0, z0);
            pos1 = new Vector3d(x1, y1, z1);
            pos2 = new Vector3d(x2, y2, z2);
            pos3 = new Vector3d(x3, y3, z3);
            color = c;
        }
    }
    
}
