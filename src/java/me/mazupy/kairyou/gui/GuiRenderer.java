package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.module.Module;
import me.mazupy.kairyou.module.ModuleManager;
import me.mazupy.kairyou.rendering.MeshBuilder;
import me.mazupy.kairyou.utils.Color;

public class GuiRenderer implements Listenable {

    private static final Color WHITE = new Color(255);
    private static final Color GRAY = new Color(120);
    private static final Color DARK_GRAY = new Color(80);
    private static final Color DARK_BLUE = new Color(0, 0, 102);

    private final float VIRTUAL_HEIGHT = 1080F;
    private final MeshBuilder mb = new MeshBuilder();

    private float CONVERSION_FACTOR = 1f;

    public void render() {
        CONVERSION_FACTOR = Kairyou.MC.getWindow().getFramebufferHeight() / VIRTUAL_HEIGHT;

        mb.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_COLOR);

        // Render modules
        final int MARGIN = 8;
        final int WIDTH = 60;
        final int HEIGHT = 12;
        int modX = MARGIN;
        int modY = MARGIN;

        for (Module mod : ModuleManager.INSTANCE.getModules()) {
            rect(modX, modY, WIDTH, HEIGHT, mod.getActive() ? DARK_BLUE : GRAY, DARK_GRAY);
            text(mod.getName(), modX + 3, modY + 3, WHITE);
            modY += HEIGHT - 1;
        }

        mb.end(false);
    }

    private void text(String text, int x, int y, Color color) {
        mb.end(false);

        final float X = convert(x);
        final float Y = convert(y);
        Kairyou.MC.textRenderer.draw(new MatrixStack(), text, X, Y, color.asARGB());

        mb.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_COLOR);
    }

    private void rect(int x, int y, int w, int h, Color fillColor, Color edgeColor) {
         quad(x, y, w, h, edgeColor);
         quad(x + 1, y + 1, w - 2, h - 2, fillColor);
    }

    private void quad(int x, int y, int w, int h, Color color) {
        final float X = convert(x);
        final float Y = convert(y);
        final float W = convert(w);
        final float H = convert(h);

        mb.vertex(X, Y, 0).colorNext(color);
        mb.vertex(X + W, Y, 0).colorNext(color);
        mb.vertex(X + W, Y + H, 0).colorNext(color);

        mb.vertex(X, Y, 0).colorNext(color);
        mb.vertex(X, Y + H, 0).colorNext(color);
        mb.vertex(X + W, Y + H, 0).colorNext(color);
    }

    private float convert(int n) { // Uses 1920x1080 scaled by height
        return n * CONVERSION_FACTOR;
    }
    
}
