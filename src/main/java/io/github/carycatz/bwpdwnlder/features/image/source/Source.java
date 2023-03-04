/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.source;

import io.github.carycatz.bwpdwnlder.features.image.Image;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface Source {
    default Image.ImageInfo get(int index) {
        return get(List.of(index)).get(0);
    }

    default List<Image.ImageInfo> get(List<Integer> indexes) {
        return getWithAction(null, indexes);
    }

    default Image.ImageInfo getLatest() {
        return get(0);
    }

    default void forEach(Consumer<Image.ImageInfo> action, List<Integer> indexes) {
        getWithAction(Objects.requireNonNull(action), indexes);
    }

    List<Image.ImageInfo> getWithAction(Consumer<Image.ImageInfo> action, List<Integer> indexes);
}
