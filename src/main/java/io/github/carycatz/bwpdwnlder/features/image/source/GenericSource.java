/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.source;

import com.google.common.base.Objects;
import io.github.carycatz.bwpdwnlder.features.image.Image;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static io.github.carycatz.bwpdwnlder.features.image.source.SourceRuntime.LOGGER;
import static io.github.carycatz.bwpdwnlder.features.image.source.SourceRuntime.executor;

public abstract class GenericSource implements Source {
    public final Image.Resolution resolution;

    protected GenericSource(Image.Resolution resolution) {
        this.resolution = resolution;
    }

    @Override
    public List<Image.ImageInfo> getWithAction(Consumer<Image.ImageInfo> action, List<Integer> indexes) {
        Map<Integer, List<Integer>> map = parseIndexes(indexes);
        LinkedList<Image.ImageInfo> list = new LinkedList<>();

        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            AtomicInteger idx = new AtomicInteger();
            executor.execute(() -> {
                try {
                    LOGGER.info("Fetching {} ...", getUrl(entry.getKey()));
                    Document document = Jsoup.connect(getUrl(entry.getKey())).get();
                    for (Element element : select(document)) {
                        if (!entry.getValue().contains(idx.getAndIncrement())) continue;
                        list.add(parse(element));
                        if (action != null) action.accept(list.getLast());
                    }
                } catch (Exception e) {
                    LOGGER.error("Cannot get the page of index {}", entry.getKey(), e);
                }
            });
        }
        return list;
    }

    protected Elements select(Document document) {
        return new Elements();
    }

    protected String getUrl(int page) {
        return null;
    }

    protected Image.ImageInfo parse(Element element) {
        return null;
    }

    protected int imageOfEachPage() {
        return 1;
    }

    protected Pair<Integer, Integer> parseIndex(int index) {
        int idx = index % imageOfEachPage();
        int page = (index - idx) / imageOfEachPage() + 1;
        return Pair.of(page, idx);
    }

    protected Map<Integer, List<Integer>> parseIndexes(List<Integer> indexes) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        indexes.forEach(i -> {
            Pair<Integer, Integer> pair = parseIndex(i);
            map.putIfAbsent(pair.getLeft(), new LinkedList<>());
            map.get(pair.getLeft()).add(pair.getRight());
        });
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericSource that)) return false;
        return resolution == that.resolution;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resolution);
    }
}
