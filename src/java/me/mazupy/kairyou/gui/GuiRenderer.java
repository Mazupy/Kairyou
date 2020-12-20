package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import me.mazupy.kairyou.Kairyou;
import me.mazupy.kairyou.rendering.MeshBuilder;
import me.mazupy.kairyou.utils.Color;

public class GuiRenderer implements Listenable {

    private static final Color WHITE = new Color(255, 255, 255); // TODO: testing
    private static final Color RED = new Color(255, 0, 0);

    private final MeshBuilder mb = new MeshBuilder();

    public void render() {
        mb.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_COLOR);

        final double X = 300;
        final double Y = 100;
        final double W = 400;
        final double H = 400;

        mb.vertex(X, Y, 0).colorNext(WHITE);
        mb.vertex(X + W, Y, 0).colorNext(WHITE);
        mb.vertex(X + W, Y + H, 0).colorNext(WHITE);

        mb.vertex(X, Y, 0).colorNext(WHITE);
        mb.vertex(X, Y + H, 0).colorNext(WHITE);
        mb.vertex(X + W, Y + H, 0).colorNext(WHITE);

        mb.end(false);

        Kairyou.MC.textRenderer.draw(new MatrixStack(), "text test", 100, 100, RED.asARGB());
    }
    
}
