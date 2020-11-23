package me.mazupy.kairyou.gui;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.render.VertexFormats;

import org.lwjgl.opengl.GL11;

import me.mazupy.kairyou.event.Render2DEvent;
import me.mazupy.kairyou.rendering.MeshBuilder;
import me.mazupy.kairyou.utils.Color;

public class GuiRenderer implements Listenable {

    public static final GuiRenderer INSTANCE = new GuiRenderer();

    private static final Color WHITE = new Color(255, 255, 255); // TODO: testing

    private final MeshBuilder mb = new MeshBuilder();

    @EventHandler
    private final Listener<Render2DEvent> on2DRender = new Listener<>(event -> {
        mb.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION_COLOR);

        final double X = 300;
        final double Y = 100;
        final double W = 400;
        final double H = 400;

        mb.vertex(X, Y, 0).endColor(WHITE);
        mb.vertex(X + W, Y, 0).endColor(WHITE);
        mb.vertex(X + W, Y + H, 0).endColor(WHITE);

        mb.vertex(X, Y, 0).endColor(WHITE);
        mb.vertex(X, Y + H, 0).endColor(WHITE);
        mb.vertex(X + W, Y + H, 0).endColor(WHITE);

        mb.end(false);
    });
    
}
