package net.neotox.client.mixin;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import net.neotox.client.ClientConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @ModifyVariable(
        method = "renderFirstPersonItem",
        at = @At("HEAD"),
        argsOnly = true,
        index = 5
    )
    private float neotox$modifySwingProgress(float swingProgress) {

        if (swingProgress <= 0.0F) {
            return swingProgress;
        }

        // StaticItems: إيقاف حركة الأداة الناتجة عن الضرب.
        if (ClientConfig.staticItems) {
            return 0.0F;
        }

        // Oldanimation: تعديل منحنى حركة الضرب.
        if (ClientConfig.oldAnimation) {
            float progress = Math.min(1.0F, swingProgress);

            return (float) Math.sin(progress * Math.PI / 2.0);
        }

        // Swingspeed: التحكم بسرعة تقدم حركة الضرب.
        if (ClientConfig.swingSpeed) {
            float speed = Math.max(
                0.0F,
                Math.min(20.0F, ClientConfig.swingSpeedValue)
            );

            float exponent = 0.25F + (speed / 20.0F) * 3.75F;

            return (float) Math.pow(
                Math.min(1.0F, swingProgress),
                exponent
            );
        }

        return swingProgress;
    }
}
