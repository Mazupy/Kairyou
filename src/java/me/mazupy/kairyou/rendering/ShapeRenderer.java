package me.mazupy.kairyou.rendering;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.MathUtils;
import me.mazupy.kairyou.utils.Rectangle;
import me.mazupy.kairyou.utils.Utils;

public class ShapeRenderer { // TODO: clean up entire file
    
    private static final float VIRTUAL_HEIGHT = 1080f;
    private static final int TEXT_INSET = 2;
    private static double conversion_factor = 1d;
    
    private static final MeshBuilder mb = new MeshBuilder();

    public static int xOffset = 0, yOffset = 0;

    public static void updateConversion() {
        conversion_factor = Kairyou.MC.getWindow().getFramebufferHeight() / VIRTUAL_HEIGHT;
    }

    private static double convert(double n) { // Uses 1920x1080 scaled by height
        return n * conversion_factor;
    }

    public static int reverseConvert(double n) {
        return MathUtils.round(n / conversion_factor);
    }

    private static float aspectRatio() {
        return Kairyou.MC.getWindow().getScaledWidth() / (float) Kairyou.MC.getWindow().getScaledHeight();
    }

    public static int maxX() {
        return MathUtils.round(aspectRatio() * VIRTUAL_HEIGHT / Kairyou.MC.getWindow().getScaleFactor());
    }

    public static int maxY() {
        return MathUtils.round(VIRTUAL_HEIGHT / Kairyou.MC.getWindow().getScaleFactor());
    }

	public static void text(String text, Rectangle dim, Color color) {
        final int X = dim.x + TEXT_INSET;
        final int Y = dim.y + (dim.h - Utils.height(text)) / 2 + TEXT_INSET;
        text(text, X, Y, color);
	}

    public static void text(String text, int x, int y, Color color) {
        final float X = (float) convert(x + xOffset);
        final float Y = (float) convert(y + yOffset);

        Kairyou.MC.textRenderer.draw(new MatrixStack(), text, X, Y, color.asARGB());
    }

    public static void shadowedText(String text, int x, int y, Color color) {
        final float X = (float) convert(x + xOffset);
        final float Y = (float) convert(y + yOffset);

        Kairyou.MC.textRenderer.drawWithShadow(new MatrixStack(), text, X, Y, color.asARGB());
    }

	public static void rect(Rectangle dim, Color fillColor, Color edgeColor) {
        quad(dim.x, dim.y, dim.w, dim.h, edgeColor);
        quad(dim.x + 1, dim.y + 1, dim.w - 2, dim.h - 2, fillColor);
	}

    public static void quad(double x, double y, double w, double h, Color color) {
        mb.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);

        final double X = convert(x + xOffset);
        final double Y = convert(y + yOffset);
        final double W = convert(w);
        final double H = convert(h);

        mb.vertex(X, Y, 0).colorNext(color);
        mb.vertex(X + W, Y, 0).colorNext(color);
        mb.vertex(X + W, Y + H, 0).colorNext(color);
        mb.vertex(X, Y + H, 0).colorNext(color);

        mb.end(false);
    }

    public static void quad(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        mb.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        
        final double X0 = convert(x0);
        final double Y0 = convert(y0);
        final double X1 = convert(x1);
        final double Y1 = convert(y1);
        final double X2 = convert(x2);
        final double Y2 = convert(y2);
        final double X3 = convert(x3);
        final double Y3 = convert(y3);

        mb.vertex(X0, Y0, 0).colorNext(color);
        mb.vertex(X1, Y1, 0).colorNext(color);
        mb.vertex(X2, Y2, 0).colorNext(color);
        mb.vertex(X3, Y3, 0).colorNext(color);

        mb.end(false);
    }

    public static void line(double x0, double y0, double x1, double y1, Color color) {
        mb.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);

        final double X0 = convert(x0 + xOffset);
        final double Y0 = convert(y0 + yOffset);
        final double X1 = convert(x1 + xOffset);
        final double Y1 = convert(y1 + yOffset);

        mb.vertex(X0, Y0, 0).colorNext(color);
        mb.vertex(X1, Y1, 0).colorNext(color);

        mb.end(false);
    }

}
