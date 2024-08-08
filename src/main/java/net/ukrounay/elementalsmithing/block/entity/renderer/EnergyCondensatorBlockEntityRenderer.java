package net.ukrounay.elementalsmithing.block.entity.renderer;


import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.entity.EnergyCondensatorBlockEntity;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.util.FastMath;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.awt.*;

import static net.minecraft.client.render.RenderPhase.END_PORTAL_PROGRAM;


public class EnergyCondensatorBlockEntityRenderer implements BlockEntityRenderer<EnergyCondensatorBlockEntity> {

    private final BlockEntityRenderDispatcher dispatcher;
    private final TextRenderer textRenderer;
    private final ItemRenderer itemRenderer;
    private int portalTicks = 0;

    public EnergyCondensatorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.dispatcher = context.getRenderDispatcher();
        this.textRenderer = context.getTextRenderer();
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public boolean rendersOutsideBoundingBox(EnergyCondensatorBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(EnergyCondensatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack stack = entity.getItem();
        float time = 0;

        World world = entity.getWorld();
        if (world != null) {
            if (!stack.isEmpty()) {
                matrices.push();
                BlockPos pos = entity.getPos();
                time = world.getTime() + tickDelta;
                double offset = Math.sin(time / 10.0) / 16;
                matrices.translate(0.5, 1.25 + offset, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 3));
                if(stack.isIn(ItemTags.SWORDS) && !stack.isOf(ModItems.UNSTABLE_AMORPHOUS_SWORD)) {
                    matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(135));
                    matrices.scale(0.65f, 0.65f, 0.65f);
                } else {
                    matrices.scale(0.5f, 0.5f, 0.5f);
                }
                int blockLighting = WorldRenderer.getLightmapCoordinates(world, pos.up());
                int itemLighting = entity.isCharging() ? 255 - (int)(20 * Math.sin((time % 20) / 20)) : blockLighting;
                itemRenderer.renderItem(stack, ModelTransformationMode.GUI, itemLighting, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(),1);
                matrices.pop();
            }

            if(this.portalTicks > 0) {
                if (entity.isCharging()) {
                    if (this.portalTicks < 20) this.portalTicks++;
                } else this.portalTicks--;
                renderCharging(entity, matrices, vertexConsumers);
            } else this.portalTicks = entity.isCharging() ? 1 : 0;
        }
    }

    protected void renderCharging(EnergyCondensatorBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        Text text = Text.of(String.valueOf(entity.ticksToCharge));
        matrices.push();
        float size = FastMath.expgrow(this.portalTicks / 20f, 2);
        float portalSize = size * (3 / 16f);

        matrices.translate(0.5, 0.60001, 0.5);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEndGateway());
        this.renderSide(matrices.peek().getPositionMatrix(), vertexConsumer,
                -portalSize/2,portalSize/2, 0.0f, 0.0f,
                portalSize/2, portalSize/2, -portalSize/2, -portalSize/2);

        matrices.translate(0, 0.0001, 0);
        Quaternionf rotation = dispatcher.camera.getRotation();
        Quaternionf adjustedRotation = new Quaternionf(0, rotation.y, 0, rotation.w)
                .rotateX((float) (Math.PI / 2));
        matrices.multiply(adjustedRotation);
        float textScale = size * 0.01f;
        matrices.scale(-textScale, -textScale, textScale);

        float h = -textRenderer.getWidth(text) / 2f;
        float y = -textRenderer.fontHeight / 2f;
        long alpha = (int) (size * 0xFF);
        long color = (alpha << 24) + 0xFFFFFF;
        textRenderer.draw(text, h, y, (int) color, false, matrices.peek().getPositionMatrix(),
                vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 255);

        matrices.pop();
    }

    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertices.vertex(model, x1, y1, z1).next();
        vertices.vertex(model, x2, y1, z2).next();
        vertices.vertex(model, x2, y2, z3).next();
        vertices.vertex(model, x1, y2, z4).next();
    }
//    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, int light) {
//        vertices.vertex(model, x1, y1, z1).texture(0,0).light(light).color(0xFFFFFFFF).next();
//        vertices.vertex(model, x2, y1, z2).texture(0,1).light(light).color(0xFFFFFFFF).next();
//        vertices.vertex(model, x2, y2, z3).texture(1,0).light(light).color(0xFFFFFFFF).next();
//        vertices.vertex(model, x1, y2, z4).texture(1,1).light(light).color(0xFFFFFFFF).next();
//    }

}
