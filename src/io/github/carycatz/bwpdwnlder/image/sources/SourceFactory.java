package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class SourceFactory {
    private static final HashMap<Pair<Sources, Image.Resolution>, Source> SOURCE_MAP = new HashMap<>(Sources.values().length * Image.Resolution.values().length);

    private SourceFactory() {
    }

    public static Source get(Sources source, Image.Resolution resolution) {
        Pair<Sources, Image.Resolution> pair = Pair.of(source, resolution);
        if (!SOURCE_MAP.containsKey(pair)) {
            synchronized (SourceFactory.class) {
                if (SOURCE_MAP.containsKey(pair)) {
                    return SOURCE_MAP.get(pair);
                }
                SOURCE_MAP.put(pair, source.constructor.apply(resolution));
            }
        }
        return SOURCE_MAP.get(pair);
    }
}
