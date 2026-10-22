package net.neotox.client.mixin;

import net.minecraft.client.render.entity.ItemEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * نقطة التوسعة الخاصة بعرض العناصر الساقطة.
 *
 * لا يغيّر هذا الكلاس شكل العناصر أو حجمها في نسخته الحالية.
 * يجب إضافة حقن العرض المناسب قبل تفعيل وظيفة Drops فعليًا.
 */
@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {
}
