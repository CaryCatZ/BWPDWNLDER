/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.source;

import io.github.carycatz.bwpdwnlder.features.image.Image;

import java.util.function.Function;

public enum Sources {
    OFFICIAL(OfficialSource::new),
    IOLIU(IoliuSource::new),
    BIMG(BimgSource::new);

    final Function<Image.Resolution, Source> constructor;

    Sources(Function<Image.Resolution, Source> constructor) {
        this.constructor = constructor;
    }

    public static Sources defaultValue() {
        return OFFICIAL;
    }
}