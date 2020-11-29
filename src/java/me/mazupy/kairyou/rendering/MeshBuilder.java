package me.mazupy.kairyou.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import org.lwjgl.opengl.GL11;

import me.mazupy.kairyou.utils.Color;

public class MeshBuilder {

    private static final int DEFAULT_BUFFER_CAPACITY = 0x100000; // 1MB (2^20)
    private final BufferBuilder buffer;

    public MeshBuilder(int capacity) {
        buffer = new BufferBuilder(capacity);
    }

    public MeshBuilder() {
        this(DEFAULT_BUFFER_CAPACITY);
    }

    public void begin(int drawMode, VertexFormat format) {
        buffer.begin(drawMode, format);
    }

    public MeshBuilder vertex(double x, double y, double z) {
        buffer.vertex(x, y, z);
        return this;
    }

    public MeshBuilder color(Color color) {
        buffer.color(color.r, color.g, color.b, color.a);
        return this;
    }

    public void endColor(Color color) {
        color(color);
        buffer.next();
    }

    public void endTexture(float u, float v) {
        buffer.texture(u, v);
        buffer.next();
    }

    public void end(boolean hasTexture) {
        GL11.glPushMatrix();

        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.disableLighting();
        if (hasTexture) RenderSystem.enableTexture();
        else RenderSystem.disableTexture();
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.lineWidth(1);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        buffer.end();
        BufferRenderer.draw(buffer);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();

        GL11.glPopMatrix();
    }
    
}
