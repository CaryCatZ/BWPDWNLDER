package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Resolution;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public enum Sources {
    OFFICIAL(OfficialSource.class),
    IOLIU(IoliuSource.class),
    BIMG(BimgSource.class);

    private final Map<Resolution, Source> sources = new HashMap<>(Resolution.values().length);

    Sources(Class<?> cls) {
        try {
            Constructor<?> constructor = cls.getDeclaredConstructor(Resolution.class);
            constructor.setAccessible(true);
            for (Resolution resolution : Resolution.values()) {
                sources.put(resolution, (Source) constructor.newInstance(resolution));
            }
        } catch (Exception ignored) {
        }
    }

    public Source resolution(int idx) {
        if (idx >= Resolution.values().length) throw new IllegalArgumentException("Invalid index");
        return sources.get(Resolution.values()[idx]);
    }
}