package net.neotox.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.function.DoubleConsumer;

public final class NeotoxScreen extends Screen {

    private int page = 0;

    private static final String[] CATEGORIES = {
            "Items",
            "Player",
            "HUD",
            "Misc",
            "Config"
    };

    private static final int ACCENT = 0xFF21D6A5;

    public NeotoxScreen() {
        super(Text.literal("NeotoxClient"));
    }

    @Override
    protected void init() {
        clearChildren();

        int left = width / 2 - 170;
        int top = height / 2 - 135;

        for (int i = 0; i < CATEGORIES.length; i++) {
            final int index = i;

            addDrawableChild(
                    ButtonWidget.builder(
                            Text.literal(CATEGORIES[i]),
                            button -> {
                                page = index;
                                clearAndInit();
                            }
                    ).dimensions(
                            left - 118,
                            top + i * 29,
                            108,
                            24
                    ).build()
            );
        }

        switch (page) {
            case 0 -> buildItemsPage(left, top);
            case 4 -> buildConfigPage(left, top);
            default -> addDrawableChild(
                    ButtonWidget.builder(
                            Text.literal("No modules added yet"),
                            button -> {
                            }
                    ).dimensions(left, top, 270, 22).build()
            );
        }
    }

    private void buildItemsPage(int x, int y) {
        addToggle(
                "StaticItems",
                ClientConfig.staticItems,
                value -> ClientConfig.staticItems = value,
                x, y
        );
        y += 29;

        addToggle(
                "Oldanimation",
                ClientConfig.oldAnimation,
                value -> ClientConfig.oldAnimation = value,
                x, y
        );
        y += 29;

        addToggle(
                "Swingspeed",
                ClientConfig.swingSpeed,
                value -> ClientConfig.swingSpeed = value,
                x, y
        );
        y += 27;

        if (ClientConfig.swingSpeed) {
            addDrawableChild(
                    new ValueSlider(
                            x, y, 270, 20,
                            "Swing speed",
                            ClientConfig.swingSpeedValue,
                            0, 20,
                            value -> ClientConfig.swingSpeedValue =
                                    (float) value,
                            false
                    )
            );
            y += 29;
        }

        addToggle(
                "Drops",
                ClientConfig.drops,
                value -> ClientConfig.drops = value,
                x, y
        );
        y += 27;

        if (ClientConfig.drops) {
            addDrawableChild(
                    ButtonWidget.builder(
                            Text.literal(
                                    "Mode: " + getDropModeName()
                            ),
                            button -> {
                                ClientConfig.dropMode =
                                        ClientConfig.dropMode
                                                == ClientConfig.DropMode.TWO_D
                                        ? ClientConfig.DropMode.THREE_D
                                        : ClientConfig.DropMode.TWO_D;

                                button.setMessage(
                                        Text.literal(
                                                "Mode: " + getDropModeName()
                                        )
                                );

                                ClientConfig.save();
                            }
                    ).dimensions(x, y, 270, 20).build()
            );
            y += 26;

            addDrawableChild(
                    new ValueSlider(
                            x, y, 270, 20,
                            "Scale",
                            ClientConfig.dropScale,
                            1, 10,
                            value -> ClientConfig.dropScale =
                                    (float) value,
                            true
                    )
            );
        }
    }

    private String getDropModeName() {
        return ClientConfig.dropMode
                == ClientConfig.DropMode.TWO_D
                ? "2D"
                : "3D";
    }

    private void buildConfigPage(int x, int y) {
        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Save configuration"),
                        button -> ClientConfig.save()
                ).dimensions(x, y, 220, 22).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Reset configuration"),
                        button -> {
                            ClientConfig.staticItems = false;
                            ClientConfig.oldAnimation = false;
                            ClientConfig.swingSpeed = false;
                            ClientConfig.swingSpeedValue = 10.0F;
                            ClientConfig.drops = false;
                            ClientConfig.dropMode =
                                    ClientConfig.DropMode.THREE_D;
                            ClientConfig.dropScale = 1.0F;

                            ClientConfig.save();
                            clearAndInit();
                        }
                ).dimensions(x, y + 30, 220, 22).build()
        );
    }

    private interface BooleanSetter {
        void set(boolean value);
    }

    private void addToggle(
            String name,
            boolean initialValue,
            BooleanSetter setter,
            int x,
            int y
    ) {
        ButtonWidget button = ButtonWidget.builder(
                toggleText(name, initialValue),
                widget -> {
                    boolean enabled =
                            !widget.getMessage().getString().endsWith("[ON]");

                    setter.set(enabled);
                    widget.setMessage(toggleText(name, enabled));

                    ClientConfig.save();
                    clearAndInit();
                }
        ).dimensions(x, y, 270, 22).build();

        addDrawableChild(button);
    }

    private Text toggleText(String name, boolean enabled) {
        return Text.literal(
                name + " [" + (enabled ? "ON" : "OFF") + "]"
        );
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {
        renderBackground(context);

        int left = width / 2 - 170;
        int top = height / 2 - 145;

        context.fill(
                left - 130,
                top,
                left + 300,
                top + 300,
                0xF20A0D13
        );

        context.fill(
                left - 130,
                top,
                left + 300,
                top + 2,
                ACCENT
        );

        context.drawText(
                textRenderer,
                "NeotoxClient",
                left - 115,
                top + 10,
                ACCENT,
                false
        );

        context.drawText(
                textRenderer,
                CATEGORIES[page],
                left,
                top + 10,
                0xFFFFFFFF,
                false
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        ClientConfig.save();
        super.close();
    }

    private static final class ValueSlider extends SliderWidget {

        private final String label;
        private final double min;
        private final double max;
        private final DoubleConsumer setter;
        private final boolean decimal;

        ValueSlider(
                int x,
                int y,
                int width,
                int height,
                String label,
                double value,
                double min,
                double max,
                DoubleConsumer setter,
                boolean decimal
        ) {
            super(
                    x, y, width, height,
                    Text.literal(label),
                    Math.max(0.0, Math.min(1.0,
                            (value - min) / (max - min)))
            );

            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;
            this.decimal = decimal;

            updateMessage();
        }

        @Override
        protected void updateMessage() {
            double value = min + this.value * (max - min);

            String formatted = decimal
                    ? String.format(Locale.ROOT, "%.1f", value)
                    : String.format(Locale.ROOT, "%.0f", value);

            setMessage(Text.literal(label + ": " + formatted));
        }

        @Override
        protected void applyValue() {
            double value = min + this.value * (max - min);
            setter.accept(value);
            ClientConfig.save();
        }
    }
}
