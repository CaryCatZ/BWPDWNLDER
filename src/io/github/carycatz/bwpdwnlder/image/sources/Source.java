package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;

import java.util.*;

public interface Source {

    static Sources source(int idx) {
        if (idx >= Sources.values().length) throw new IllegalArgumentException("Invalid index");
        return Sources.values()[idx];
    }

    Image.ImageInfo get(int index);

    default List<Image.ImageInfo> gets(List<Integer> indexes) {
        List<Image.ImageInfo> list = new LinkedList<>();
        indexes.forEach(i -> list.add(get(i)));
        return list;
    }

    default Image.ImageInfo getLatest() {
        return get(0);
    }
}
