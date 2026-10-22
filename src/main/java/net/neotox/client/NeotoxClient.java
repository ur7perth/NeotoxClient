package net.neotox.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFW;

public final class NeotoxClient implements ClientModInitializer {

    public static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        // تحميل الإعدادات المحفوظة
        ClientConfig.load();

        // تسجيل زر فتح القائمة
        menuKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.neotoxclient.open_menu",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_RIGHT_SHIFT,
                        "category.neotoxclient"
                )
        );

        // الاستجابة لضغط الزر
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.player == null) {
                    continue;
                }

                if (client.currentScreen == null) {
                    client.setScreen(new NeotoxScreen());
                } else if (client.currentScreen instanceof NeotoxScreen) {
                    client.setScreen(null);
                }
            }
        });
    }
}
