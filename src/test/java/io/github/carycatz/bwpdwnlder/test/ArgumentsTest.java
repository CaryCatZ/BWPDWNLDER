package io.github.carycatz.bwpdwnlder.test;

import io.github.carycatz.bwpdwnlder.application.main.Arguments;
import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Sources;
import org.apache.logging.log4j.spi.StandardLevel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsTest {
    @Test
    void test() {
        Arguments arguments;
        for (Image.Resolution resolution : Image.Resolution.values()) {
            for (Sources sources : Sources.values()) {
                for (StandardLevel level : StandardLevel.values()) {
                    List<Integer> indexes = IntStream.range(0, 8).boxed().toList();
                    StringBuilder sb = new StringBuilder(32);
                    indexes.forEach(integer -> sb.append(integer).append(" "));
                    arguments = new Arguments();
                    arguments.parse("%s-res %s -src %s -L %s".formatted(sb, resolution, sources, level).split(" "));
                    assertArrayEquals(indexes.toArray(), arguments.indexes.toArray());
                    assertEquals(resolution, arguments.resolution);
                    assertEquals(sources, arguments.source);
                    assertEquals(level, arguments.logLvl);
                }
            }
        }
    }
}
