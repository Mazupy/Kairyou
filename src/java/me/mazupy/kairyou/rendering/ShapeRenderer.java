package me.mazupy.kairyou.rendering;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;

import org.lwjgl.opengl.GL11;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.MathUtils;

public class ShapeRenderer {
    
    private static final float VIRTUAL_HEIGHT = 1080f;
    private static float conversion_factor = 1f;
    
    private static final MeshBuilder mb = new MeshBuilder();

    public static void updateConversion() {
        conversion_factor = Kairyou.MC.getWindow().getFramebufferHeight() / VIRTUAL_HEIGHT;
    }

    private static float convert(int n) { // Uses 1920x1080 scaled by height
        return n * conversion_factor;
    }

    public static int reverseConvert(double n) {
        return MathUtils.round(n / conversion_factor);
    }

    public static int maxY() {
        return MathUtils.round(VIRTUAL_HEIGHT / Kairyou.MC.getWindow().getScaleFactor());
    }

    public static void text(String text, int x, int y, Color color) {
        final float X = convert(x);
        final float Y = convert(y);

        Kairyou.MC.textRenderer.draw(new MatrixStack(), text, X, Y, color.asARGB());
    }

    public static void shadowedText(String text, int x, int y, Color color) {
        final float X = convert(x);
        final float Y = convert(y);

        Kairyou.MC.textRenderer.drawWithShadow(new MatrixStack(), text, X, Y, color.asARGB());
    }

    public static void rect(int x, int y, int w, int h, Color fillColor, Color edgeColor) {
        quad(x, y, w, h, edgeColor);
        quad(x + 1, y + 1, w - 2, h - 2, fillColor);
    }

    private static void quad(int x, int y, int w, int h, Color color) {
        mb.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);

        final float X = convert(x);
        final float Y = convert(y);
        final float W = convert(w);
        final float H = convert(h);

        mb.vertex(X, Y, 0).colorNext(color);
        mb.vertex(X + W, Y, 0).colorNext(color);
        mb.vertex(X + W, Y + H, 0).colorNext(color);
        mb.vertex(X, Y + H, 0).colorNext(color);

        mb.end(false);
    }

    public static void line(int x0, int y0, int x1, int y1, Color color) {
        mb.begin(GL11.GL_LINE, VertexFormats.POSITION_COLOR);

        final float X = convert(x0);
        final float Y = convert(y0);
        final float W = convert(x1);
        final float H = convert(y1);

        mb.vertex(X, Y, 0).colorNext(color);
        mb.vertex(X + W, Y + H, 0).colorNext(color);

        mb.end(false);
    }

}
