package net.ukrounay.elementalsmithing.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import static net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE_LIGHT;
import static net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_LIGHT;

public class ModRenderLayers extends RenderLayer {

    // RenderLayer has no public constructor either — this satisfies the compiler,
    // it's never actually instantiated.
    private ModRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode,
                            int expectedBufferSize, boolean hasCrumbling, boolean translucent,
                            Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }


    private static final Identifier PARALLAX_CIRCUIT_TEXTURE =
            new Identifier("elementalsmithing", "textures/block/parallax_circuit.png");

    private static RenderLayer PARALLAX_CIRCUIT_LAYER;

    public static RenderLayer getParallaxCircuit() {
        if (PARALLAX_CIRCUIT_LAYER == null) {
            RenderLayer.MultiPhaseParameters phases = RenderLayer.MultiPhaseParameters.builder()
                    .program(POSITION_COLOR_TEXTURE_LIGHTMAP_PROGRAM)
                    .texture(new RenderPhase.Texture(PARALLAX_CIRCUIT_TEXTURE, false, false))
                    .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                    .cull(RenderPhase.DISABLE_CULLING)
                    .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                    .overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
                    .writeMaskState(RenderPhase.COLOR_MASK)
                    .build(false);

            PARALLAX_CIRCUIT_LAYER = RenderLayer.of(
                    "elementalsmithing_parallax_circuit",
                    POSITION_COLOR_TEXTURE_LIGHT,
                    VertexFormat.DrawMode.QUADS,
                    256,
                    false,   // hasCrumbling — you're not overlaying block-break cracks on this
                    true,    // translucent — enables sorting with other translucent geometry
                    phases
            );
        }
        return PARALLAX_CIRCUIT_LAYER;
    }
}