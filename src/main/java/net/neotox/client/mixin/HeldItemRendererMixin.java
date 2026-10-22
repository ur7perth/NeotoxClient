package net.neotox.client.mixin;

import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * نقطة التوسعة الخاصة بعرض العنصر في يد اللاعب.
 *
 * يحتاج StaticItems وOldanimation وSwingspeed إلى تنفيذ
 * تعديلات عرض فعلية واختبارها على Minecraft 1.20.1.
 */
@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
}
