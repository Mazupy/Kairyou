package me.mazupy.kairyou.rendering;

import java.util.ArrayList;
import java.util.List;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Vec2f;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.event.Render2DEvent;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.MathUtils;

public class OverlayProjector implements Listenable {

    /**
     * This only class only exists because I couldn't get anything to render on the worldRenderEvent and 
     * I couldn't figure out directly rendering 3D!
     * If you know why/how, please tell me, thank you in advance!
     */

    private static float cameraYaw() {
        final float rawYaw = Kairyou.MC.gameRenderer.getCamera().getYaw();
        final float modYaw = Math.signum(rawYaw) * Math.abs(rawYaw) % 360;
        if (modYaw > 180) return modYaw - 360;
        if (modYaw <= -180) return modYaw + 360;
        return modYaw;
    }

    // TODO: clean up this mess when you don't have to trial and error OpenGL while figuring out projection math for over 26 hours
    // If anyone is sucessful in using simpler math / projection matrices, please do a PR

    private static List<LineRenderTask> lineRenderQueue = new ArrayList<>(1024);
    private static List<QuadRenderTask> quadRenderQueue = new ArrayList<>(1024);

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

    private static Vec2f projection(Vector3f pos) {
        double x = pos.getX() - Kairyou.MC.gameRenderer.getCamera().getPos().x;
        double y = pos.getY() - Kairyou.MC.gameRenderer.getCamera().getPos().y;
        double z = pos.getZ() - Kairyou.MC.gameRenderer.getCamera().getPos().z;
        z *= -1; // WTF? I checked the formulas 5 times on Wikipedia and found no errors, why is this necessary?
        // https://en.wikipedia.org/wiki/3D_projection#Mathematical_formula (also where the following is derived from)
        double thetaX = -Kairyou.MC.gameRenderer.getCamera().getPitch() * MathUtils.DEG2RAD;
        double thetaY = cameraYaw() * MathUtils.DEG2RAD;
        double eX = ShapeRenderer.maxX() / 2;
        double eY = ShapeRenderer.maxY() / 2;
        double eZ = ShapeRenderer.maxY() / 2 * MathUtils.degTan(90 - Kairyou.MC.options.fov / 2);
        double sX = Math.sin(thetaX);
        double sY = Math.sin(thetaY);
        double cX = Math.cos(thetaX);
        double cY = Math.cos(thetaY);
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
        Vector3f pos0;
        Vector3f pos1;

        LineRenderTask(double x0, double y0, double z0, double x1, double y1, double z1, Color c) {
            pos0 = new Vector3f((float) x0, (float) y0, (float) z0);
            pos1 = new Vector3f((float) x1, (float) y1, (float) z1);
            color = c;
        }
    }

    protected static class QuadRenderTask {
        Color color;
        Vector3f pos0;
        Vector3f pos1;
        Vector3f pos2;
        Vector3f pos3;

        QuadRenderTask(double x0, double y0, double z0, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, Color c) {
            pos0 = new Vector3f((float) x0, (float) y0, (float) z0);
            pos1 = new Vector3f((float) x1, (float) y1, (float) z1);
            pos2 = new Vector3f((float) x2, (float) y2, (float) z2);
            pos3 = new Vector3f((float) x3, (float) y3, (float) z3);
            color = c;
        }
    }
    
}
