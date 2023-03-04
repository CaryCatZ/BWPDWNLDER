/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.test;

import io.github.carycatz.bwpdwnlder.features.image.Image;
import io.github.carycatz.bwpdwnlder.features.image.source.Source;
import io.github.carycatz.bwpdwnlder.features.image.source.SourceFactory;
import io.github.carycatz.bwpdwnlder.features.image.source.Sources;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime.LOGGER;
import static org.junit.jupiter.api.Assertions.*;

class SourcesTest {
    @Test
    void test() {
        for (Sources source : Sources.values()) {
            assertTimeout(Duration.ofSeconds(25), () -> {
                test(source);
                LOGGER.info("Passed");
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