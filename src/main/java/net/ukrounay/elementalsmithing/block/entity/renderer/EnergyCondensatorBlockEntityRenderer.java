package net.ukrounay.elementalsmithing.block.entity.renderer;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.block.entity.EnergyCondensatorBlockEntity;
import org.joml.Matrix4f;


public class EnergyCondensatorBlockEntityRenderer implements BlockEntityRenderer<EnergyCondensatorBlockEntity> {

    private final BlockEntityRenderDispatcher dispatcher;
    private final TextRenderer textRenderer;

    public EnergyCondensatorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.dispatcher = context.getRenderDispatcher();
        this.textRenderer = context.getTextRenderer();
    }

    @Override
    public boolean rendersOutsideBoundingBox(EnergyCondensatorBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(EnergyCondensatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) return;
        matrices.push();

        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        float time = world.getTime() + tickDelta;
        double offset = Math.sin(time / 10.0) / 16;
        matrices.translate(0.5, 1.25 + offset, 0.5);
        matrices.scale(0.4f, 0.4f, 0.4f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 3));
        int lighting = entity.isCharging() ? 194 : WorldRenderer.getLightmapCoordinates(world, pos.up());
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, lighting, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(),1);

        matrices.pop();

        if(entity.isCharging())
            renderNumber(entity, matrices, vertexConsumers, 225);
    }

    protected void renderNumber(EnergyCondensatorBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Text text = Text.of(String.valueOf(entity.ticksToCharge));
        matrices.push();
        matrices.translate(0.5, 0.95, 0.5);
        matrices.multiply(dispatcher.camera.getRotation());
        matrices.scale(-0.0125f, -0.0125f, 0.0125f);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float h = -textRenderer.getWidth(text) / 2f;
        textRenderer.draw(text, h, 0, 0xBBFFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
        matrices.pop();
    }
}
