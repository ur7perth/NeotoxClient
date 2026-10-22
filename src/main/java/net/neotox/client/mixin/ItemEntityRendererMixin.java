package net.neotox.client.mixin;

import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.ItemEntity;

import net.neotox.client.ClientConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {

    @Inject(
        method = "render",
        at = @At("HEAD")
    )
    private void neotox$scaleDroppedItems(
        ItemEntity entity,
        float yaw,
        float tickDelta,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        if (!ClientConfig.drops) {
            return;
        }

        float scale = Math.max(
            1.0F,
            Math.min(10.0F, ClientConfig.dropScale)
        );

        matrices.push();
        matrices.scale(scale, scale, scale);
    }

    @Inject(
        method = "render",
        at = @At("RETURN")
    )
    private void neotox$restoreDroppedItems(
        ItemEntity entity,
        float yaw,
        float tickDelta,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        if (ClientConfig.drops) {
            matrices.pop();
        }
    }
}
