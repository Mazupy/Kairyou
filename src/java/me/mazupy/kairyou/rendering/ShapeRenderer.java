package me.mazupy.kairyou.rendering;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import me.mazupy.kairyou.utils.Color;
import me.mazupy.kairyou.utils.MathUtils;
import me.mazupy.kairyou.utils.Rectangle;
import me.mazupy.kairyou.utils.Utils;

import static me.mazupy.kairyou.Kairyou.*;

public class ShapeRenderer { // TODO: clean up entire file
    
    private static final float VIRTUAL_HEIGHT = 1080f;
    private static final int TEXT_INSET = 2;
    private static double conversion_factor = 1d;
    
    private static final MeshBuilder mb = new MeshBuilder();

    public static int xOffset = 0, yOffset = 0;

    public static void updateConversion() {
        conversion_factor = MC.getWindow().getFramebufferHeight() / VIRTUAL_HEIGHT;
    }

    private static double convert(double n) { // Uses 1920x1080 scaled by height
        return n * conversion_factor;
    }

    public static double reverseConvert(double n) {
        return n / conversion_factor;
    }

    private static float aspectRatio() {
        return MC.getWindow().getScaledWidth() / (float) MC.getWindow().getScaledHeight();
    }

    public static int maxX() {
        return MathUtils.round(aspectRatio() * VIRTUAL_HEIGHT / MC.getWindow().getScaleFactor());
    }

    public static int maxY() {
        return MathUtils.round(VIRTUAL_HEIGHT / MC.getWindow().getScaleFactor());
    }

    public static void text(String text, Rectangle dim, Color color) {
        textAlign(text, dim, color, Alignment.Left, false);
    }

    public static void textAlign(String text, Rectangle dim, Color color, Alignment align, boolean shadowed) {
        int wOff = 0;
        int hOff = 0;
        switch (align) {
            case TopMid: case Center: case BottomMid:
                wOff = dim.w / 2;
                break;
            case TopRight: case Right: case BottomRight:
                wOff = dim.w;
                break;
            default:
                // nothing
        }
        switch (align) {
            case Left: case Center: case Right:
                hOff = dim.h / 2;
                break;
            case BottomLeft: case BottomMid: case BottomRight:
                hOff = dim.h;
                break;
            default:
                // nothing
        }
        textAlign(text, dim.x + wOff, dim.y + hOff, color, align, shadowed);
    }

    public static void textAlign(String text, int x, int y, Color color, Alignment align, boolean shadowed) {
        final int X = x + alignX(align, Utils.width(text), TEXT_INSET);
        final int Y = y + alignY(align, Utils.height(text), TEXT_INSET);

        if (shadowed) shadowedText(text, X, Y, color);
        else text(text, X, Y, color);
    }

    public static void text(String text, int x, int y, Color color) {
        final float X = (float) convert(x + xOffset);
        final float Y = (float) convert(y + yOffset);

        MC.textRenderer.draw(new MatrixStack(), text, X, Y, color.asARGB());
    }

    public static void shadowedText(String text, int x, int y, Color color) {
        final float X = (float) convert(x + xOffset);
        final float Y = (float) convert(y + yOffset);

        MC.textRenderer.drawWithShadow(new MatrixStack(), text, X, Y, color.asARGB());
    }

    public static void rect(Rectangle dim, Color fillColor, Color edgeColor) {
        quad(dim.x, dim.y, dim.w, dim.h, edgeColor);
        quad(dim.x + 1, dim.y + 1, dim.w - 2, dim.h - 2, fillColor);
    }

    public static void background(Color color) {
        quad(0, 0, maxX(), maxY(), color);
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

    public static void item(ItemStack item, int x, int y, Alignment align) {
        final int X = MathUtils.round(convert(x + xOffset)) + alignX(align, Utils.ITEM_SIZE);
        final int Y = MathUtils.round(convert(y + yOffset)) + alignY(align, Utils.ITEM_SIZE);

        MC.getItemRenderer().renderInGuiWithOverrides(item, X, Y);
        MC.getItemRenderer().renderGuiItemOverlay(MC.textRenderer, item, X, Y);
    }

    private static int alignX(Alignment align, int w) {
        return alignX(align, w, 0);
    }

    private static int alignX(Alignment align, int w, int inset) {
        switch (align) {
            case TopMid: case Center: case BottomMid:
                return -w / 2;
            case TopRight: case Right: case BottomRight: 
                return -w - inset;
            default:
                return inset;
        }
    }

    private static int alignY(Alignment align, int h) {
        return alignY(align, h, 0);
    }

    private static int alignY(Alignment align, int h, int inset) {
        switch (align) {
            case Left: case Center: case Right:
                return -h / 2 + inset;
            case BottomLeft: case BottomMid: case BottomRight:
                return -h;
            default:
                return inset;
        }
    }

    public enum Alignment {
        TopLeft,
        TopMid,
        TopRight,
        Left,
        Center,
        Right,
        BottomLeft,
        BottomMid,
        BottomRight
    }

}
