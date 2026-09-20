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
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.block.entity.EnergyCondensatorBlockEntity;
import net.ukrounay.elementalsmithing.client.render.ModRenderLayers;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.util.FastMath;
import net.ukrounay.elementalsmithing.util.RotationHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class EnergyCondensatorBlockEntityRenderer implements BlockEntityRenderer<EnergyCondensatorBlockEntity> {

    private static final float FRAME_HALF_SIZE = 3 / 32f;
    private static final int PIXEL_DENSITY = 4;

    private final BlockEntityRenderDispatcher dispatcher;
    private final TextRenderer textRenderer;
    private final ItemRenderer itemRenderer;

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
        World world = entity.getWorld();
        if (world == null) return;

        if (entity.getStorageOwner() != entity) return;

        ItemStack stack = entity.getItem();
        float time = world.getTime() + tickDelta;
        boolean charging = entity.isCharging();

        Direction facing = entity.getCachedState().get(Properties.FACING);

//        if(entity.portalTicks > 0) {
//            if (charging) {
//                if (entity.portalTicks < 20) entity.portalTicks++;
//            } else entity.portalTicks--;
//
//            matrices.push();
//            renderScreen(entity, matrices, vertexConsumers, light, time, facing);
//            matrices.pop();
//
//        } else entity.portalTicks = charging ? 1 : 0;

        if (!stack.isEmpty()) {
            matrices.push();
            renderStack(entity, tickDelta, matrices, vertexConsumers, light, time, stack, world, facing);
            matrices.pop();
        }

    }


//    private void renderStack(EnergyCondensatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float time, ItemStack stack, World world, Direction facing) {
//
//        Vec3i dir = facing.getVector();
//        float floatDist = 0.75f + (float) (Math.sin(time / 10.0) / 16);
//
//        matrices.translate(
//            0.5 + dir.getX() * floatDist,
//            0.5 + dir.getY() * floatDist,
//            0.5 + dir.getZ() * floatDist
//        );
//
//        Vector3f interpolated = new Vector3f(entity.prevItemOffset).lerp(entity.itemOffset, tickDelta);
//        matrices.translate(0.5 + interpolated.x, 0.5 + interpolated.y, 0.5 + interpolated.z);
//
////        RotationHelper.applyFacingRotation(matrices, facing);
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 3));
//
//
//        if(stack.isIn(ItemTags.SWORDS) && !stack.isOf(ModItems.UNSTABLE_AMORPHOUS_SWORD)) {
//            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(135));
//            matrices.scale(0.65f, 0.65f, 0.65f);
//        } else {
//            matrices.scale(0.5f, 0.5f, 0.5f);
//        }
//
//
//        itemRenderer.renderItem(stack, ModelTransformationMode.GUI, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, world,1);
//    }

private void renderStack(EnergyCondensatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float time, ItemStack stack, World world, Direction facing) {
    Vec3i dir = facing.getVector();
    float floatDist = 0.75f + (float) (Math.sin(time / 10.0) / 16);

    matrices.translate(
            0.5 + dir.getX() * floatDist,
            0.5 + dir.getY() * floatDist,
            0.5 + dir.getZ() * floatDist
    );

    RotationHelper.applyFacingRotation(matrices, facing);

    Quaternionf interpolatedOffset = new Quaternionf(entity.prevRotationOffset).slerp(entity.rotationOffset, tickDelta);
    matrices.multiply(interpolatedOffset);

    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 3));

    if (stack.isIn(ItemTags.SWORDS) && !stack.isOf(ModItems.UNSTABLE_AMORPHOUS_SWORD)) {
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(135));
        matrices.scale(0.65f, 0.65f, 0.65f);
    } else {
        matrices.scale(0.5f, 0.5f, 0.5f);
    }

    itemRenderer.renderItem(stack, ModelTransformationMode.GUI, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, world, 1);
}



//    private final Quaternionf adjustedRotation = new Quaternionf();

    private void renderScreen(EnergyCondensatorBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float time, Direction facing) {

        float size = FastMath.expgrow(entity.portalTicks / 20f, 2);

        matrices.translate(0.5, 0.60001, 0.5);
        RotationHelper.applyFacingRotation(matrices, facing);

        renderParallaxCircuit(matrices, vertexConsumers.getBuffer(ModRenderLayers.getParallaxCircuit()), time, size, light);

        Vector3f textElevation = new Vector3f(0, 0.0001f, 0);
        RotationHelper.applyFacingRotation(textElevation, facing);
        matrices.translate(textElevation.x(), textElevation.y(), textElevation.z());

//        Quaternionf rotation = dispatcher.camera.getRotation();
//        adjustedRotation.set(0, rotation.y, 0, rotation.w).rotateX((float) (Math.PI / 2));
//        matrices.multiply(adjustedRotation);


        float textScale = size * 0.01f;
        matrices.scale(-textScale, -textScale, textScale);

        float h = -textRenderer.getWidth(entity.cachedText) / 2f;
        float y = -textRenderer.fontHeight / 2f;
        long alpha = (int) (size * 0xFF);
        long color = (alpha << 24) + 0xFFFFFF;
        textRenderer.draw(entity.cachedText, h, y, (int) color, false, matrices.peek().getPositionMatrix(),
                vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
    }


    private void renderParallaxCircuit(MatrixStack matrices, VertexConsumer vc, float time, float growth, int light) {
        Matrix4f model = matrices.peek().getPositionMatrix();

        emitLayer(model, vc, 0, 0f, growth, 1.0f, light);
        emitLayer(model, vc, -time * 0.015f, 0.5f, growth, 0.65f, light);
        emitLayer(model, vc, 0.5f, -time * 0.015f, growth, 0.65f, light);
        emitLayer(model, vc, time * 0.030f, 0.25f, growth, 0.33f, light);
        emitLayer(model, vc, 0.25f, time * 0.030f, growth, 0.33f, light);
    }

    private void emitLayer(Matrix4f model, VertexConsumer vc, float uScroll, float vScroll, float growth, float alphaMul, int light) {
        float half = FRAME_HALF_SIZE * growth;
        int a = MathHelper.clamp((int) (alphaMul * 255), 0, 255);
        float u0 = uScroll % 1.0f - half * PIXEL_DENSITY;
        float v0 = vScroll % 1.0f - half * PIXEL_DENSITY;
        float u1 = u0 + half * 2 * PIXEL_DENSITY;
        float v1 = v0 + half * 2 * PIXEL_DENSITY;

        vc.vertex(model, -half, 0, -half).color(255, 255, 255, a).texture(u0, v0).light(light).next();
        vc.vertex(model, -half, 0,  half).color(255, 255, 255, a).texture(u0, v1).light(light).next();
        vc.vertex(model,  half, 0,  half).color(255, 255, 255, a).texture(u1, v1).light(light).next();
        vc.vertex(model,  half, 0, -half).color(255, 255, 255, a).texture(u1, v0).light(light).next();
    }


}
