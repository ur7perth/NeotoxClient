package net.neotox.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ClientConfig {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final Path PATH =
            FabricLoader.getInstance().getConfigDir()
                    .resolve("neotoxclient.json");

    public static boolean staticItems = false;
    public static boolean oldAnimation = false;
    public static boolean swingSpeed = false;
    public static double swingSpeedValue = 10.0;

    public static boolean drops = false;
    public static DropMode dropMode = DropMode.THREE_D;
    public static double dropScale = 1.0;

    public enum DropMode {
        TWO_D,
        THREE_D
    }

    private static class Data {
        boolean staticItems = false;
        boolean oldAnimation = false;
        boolean swingSpeed = false;
        double swingSpeedValue = 10.0;

        boolean drops = false;
        DropMode dropMode = DropMode.THREE_D;
        double dropScale = 1.0;
    }

    public static void load() {
        try {
            if (!Files.exists(PATH)) {
                save();
                return;
            }

            try (Reader reader = Files.newBufferedReader(PATH)) {
                Data data = GSON.fromJson(reader, Data.class);

                if (data == null) {
                    return;
                }

                staticItems = data.staticItems;
                oldAnimation = data.oldAnimation;
                swingSpeed = data.swingSpeed;

                swingSpeedValue = clamp(
                        data.swingSpeedValue, 0.0, 20.0
                );

                drops = data.drops;

                dropMode = data.dropMode == null
                        ? DropMode.THREE_D
                        : data.dropMode;

                dropScale = clamp(data.dropScale, 1.0, 10.0);
            }
        } catch (Exception exception) {
            System.err.println(
                    "[NeotoxClient] Could not load configuration: "
                            + exception.getMessage()
            );
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());

            Data data = new Data();

            data.staticItems = staticItems;
            data.oldAnimation = oldAnimation;
            data.swingSpeed = swingSpeed;
            data.swingSpeedValue = swingSpeedValue;

            data.drops = drops;
            data.dropMode = dropMode;
            data.dropScale = dropScale;

            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (Exception exception) {
            System.err.println(
                    "[NeotoxClient] Could not save configuration: "
                            + exception.getMessage()
            );
        }
    }

    public static double clamp(
            double value,
            double min,
            double max
    ) {
        return Math.max(min, Math.min(max, value));
    }

    private ClientConfig() {
    }
}
