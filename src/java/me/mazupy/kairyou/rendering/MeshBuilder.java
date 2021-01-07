package me.mazupy.kairyou.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;

import me.mazupy.kairyou.utils.Color;

import static com.mojang.blaze3d.systems.RenderSystem.*;
import static org.lwjgl.opengl.GL11.*;

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

    public void colorNext(Color color) {
        color(color);
        buffer.next();
    }

    public void textureNext(float u, float v) {
        buffer.texture(u, v);
        buffer.next();
    }

    public void end(boolean hasTexture) {
        glPushMatrix();

        disableAlphaTest();
        enableBlend();
        blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        disableCull();
        disableDepthTest();
        disableLighting();
        if (hasTexture) enableTexture();
        else disableTexture();
        color4f(1, 1, 1, 1);
        lineWidth(1);
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL_SMOOTH);

        buffer.end();
        BufferRenderer.draw(buffer);

        glDisable(GL_LINE_SMOOTH);
        enableTexture();
        enableDepthTest();
        enableAlphaTest();

        glPopMatrix();
    }
    
}
