package io.github.carycatz.bwpdwnlder.test;

import io.github.carycatz.bwpdwnlder.application.main.Main;
import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Sources;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static io.github.carycatz.bwpdwnlder.application.lifecycle.LifeCycle.LOGGER;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class MainTest {
    private static Path tempDir;

    static {
        assertDoesNotThrow(() -> tempDir = Files.createTempDirectory("TEST-BWPDWNLDER-"));
    }

    @Test
    void test() {
        int lim = 8;
        int initiation = 7;
        if (System.getProperty("bwpdwnlder.test.long-test") != null) {
            lim *= 15;
        }
        for (int i = initiation; i < lim; i++) {
            LOGGER.info("=".repeat(42));
            LOGGER.info(" ".repeat(15) + "Test Round {}", i);
            LOGGER.info("=".repeat(42));
            test(i);
        }
    }

    private static void assertAndClean(Path path, int count) {
        assertEquals(Objects.requireNonNull(
                path.toFile().listFiles(
                        (dir, name) -> name.endsWith(".jpg")
                )
        ).length, count);

        for (File file : Objects.requireNonNull(path.toFile().listFiles())) {
            if (!file.delete()) {
                LOGGER.warn("Cannot delete file {}", file);
            }
        }
    }

    private static void test(int count) {
        for (Image.Resolution resolution : Image.Resolution.values()) {
            LOGGER.info("=".repeat(42));
            LOGGER.info(" ".repeat(11) + "Test - {}", resolution);
            LOGGER.info("=".repeat(42));
            for (Sources source : Sources.values()) {
                LOGGER.info("=".repeat(42));
                LOGGER.info(" ".repeat(11) + "Test {} - {}", resolution, source);
                LOGGER.info("=".repeat(42));
                assertTimeoutPreemptively(Duration.ofSeconds(20L * count), () -> {
                    String arg = buildArg(IntStream.range(0, count).boxed(), Map.of(
                            "-o", tempDir,
                            "-src", source,
                            "-res", resolution
                    ));

                    Main.main(arg.split(" "));
                    assertAndClean(tempDir, count);

                    System.setProperty("bwpdwnlder.args", arg);
                    Main.main(new String[]{});
                    assertAndClean(tempDir, count);
                });
            }
        }
    }

    private static <T> String buildArg(Stream<T> stream, Map<String, Object> args) {
        StringBuilder sb = new StringBuilder(128);
        for (Object o : stream.toArray()) {
            sb.append(o).append(" ");
        }
        for (Map.Entry<String, Object> arg : args.entrySet()) {
            sb.append(arg.getKey()).append(" ").append(arg.getValue()).append(" ");
        }
        return sb.toString();
    }

    @AfterAll
    static void tearDown() {
        tempDir = null;
    }
}