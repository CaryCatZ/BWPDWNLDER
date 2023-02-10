package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;

import java.util.function.Function;

public enum Sources {
    OFFICIAL(OfficialSource::new),
    IOLIU(IoliuSource::new),
    BIMG(BimgSource::new);

    final Function<Image.Resolution, Source> constructor;

    Sources(Function<Image.Resolution, Source> constructor) {
        this.constructor = constructor;
    }
}