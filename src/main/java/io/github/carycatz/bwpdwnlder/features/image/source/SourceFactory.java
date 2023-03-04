/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.source;

import io.github.carycatz.bwpdwnlder.features.image.Image;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.WeakHashMap;

public final class SourceFactory {
    private static final Map<Pair<Sources, Image.Resolution>, Source> SOURCE_CACHE = new WeakHashMap<>(Sources.values().length);

    private SourceFactory() {
        throw new InstantiationError();
    }

    public static Source get(Sources source, Image.Resolution resolution) {
        Pair<Sources, Image.Resolution> pair = Pair.of(source, resolution);
        if (!SOURCE_CACHE.containsKey(pair)) {
            synchronized (SourceFactory.class) {
                if (SOURCE_CACHE.containsKey(pair)) {
                    return SOURCE_CACHE.get(pair);
                }
                SOURCE_CACHE.put(pair, source.constructor.apply(resolution));
            }
        }
        return SOURCE_CACHE.get(pair);
    }

    public static Source defaultSource() {
        return get(Sources.defaultValue(), Image.Resolution.defaultValue());
    }
}
