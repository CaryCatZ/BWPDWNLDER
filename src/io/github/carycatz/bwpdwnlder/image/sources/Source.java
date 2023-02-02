package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
