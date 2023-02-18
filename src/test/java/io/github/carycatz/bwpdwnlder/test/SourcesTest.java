package io.github.carycatz.bwpdwnlder.test;

import io.github.carycatz.bwpdwnlder.application.lifecycle.LifeCycle;
import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Source;
import io.github.carycatz.bwpdwnlder.image.sources.SourceFactory;
import io.github.carycatz.bwpdwnlder.image.sources.Sources;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SourcesTest {
    @Test
    void test() {
        for (Sources source : Sources.values()) {
            assertTimeout(Duration.ofSeconds(25), () -> {
                test(source);
                LifeCycle.LOGGER.info("Passed");
            });
        }
    }

    private static void test(Sources sources) {
        Source source = SourceFactory.get(sources, Image.Resolution.R_UHD);
        List<Image.ImageInfo> results = new ArrayList<>();

        Image.ImageInfo latest = source.getLatest();
        List<Image.ImageInfo> list = source.getWithAction(results::add, List.of(0, 1, 2, 3));
        assertArrayEquals(results.toArray(), list.toArray());
        assertEquals(latest, list.get(0));
    }
}