package net.neotox.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ClientConfig {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final Path CONFIG_PATH =
            Path.of("config", "neotoxclient.json");

    public static boolean staticItems = false;
    public static boolean oldAnimation = false;
    public static boolean swingSpeed = false;

    // من 0 إلى 20
    public static float swingSpeedValue = 10.0F;

    public static boolean drops = false;

    public enum DropMode {
        TWO_D,
        THREE_D
    }

    public static DropMode dropMode = DropMode.THREE_D;

    // من 1 إلى 10
    public static float dropScale = 1.0F;

    private ClientConfig() {
    }

    public static void load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                save();
                return;
            }

            String json = Files.readString(CONFIG_PATH);
            Settings settings = GSON.fromJson(json, Settings.class);

            if (settings != null) {
                staticItems = settings.staticItems;
                oldAnimation = settings.oldAnimation;
                swingSpeed = settings.swingSpeed;
                swingSpeedValue = clamp(settings.swingSpeedValue, 0, 20);
                drops = settings.drops;
                dropMode = settings.dropMode != null
                        ? settings.dropMode
                        : DropMode.THREE_D;
                dropScale = clamp(settings.dropScale, 1, 10);
            }
        } catch (Exception e) {
            System.err.println(
                    "[NeotoxClient] Failed to load configuration: "
                            + e.getMessage()
            );
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            Settings settings = new Settings();
            settings.staticItems = staticItems;
            settings.oldAnimation = oldAnimation;
            settings.swingSpeed = swingSpeed;
            settings.swingSpeedValue = swingSpeedValue;
            settings.drops = drops;
            settings.dropMode = dropMode;
            settings.dropScale = dropScale;

            Files.writeString(CONFIG_PATH, GSON.toJson(settings));
        } catch (IOException e) {
            System.err.println(
                    "[NeotoxClient] Failed to save configuration: "
                            + e.getMessage()
            );
        }
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static final class Settings {
        boolean staticItems;
        boolean oldAnimation;
        boolean swingSpeed;
        float swingSpeedValue = 10.0F;
        boolean drops;
        DropMode dropMode = DropMode.THREE_D;
        float dropScale = 1.0F;
    }
}
