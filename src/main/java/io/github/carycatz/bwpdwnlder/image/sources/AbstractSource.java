package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.carycatz.bwpdwnlder.application.lifecycle.LifeCycle.LOGGER;

abstract class AbstractSource implements Source {
    public static final String BING = "https://cn.bing.com";

    public final Image.Resolution resolution;

    protected AbstractSource(Image.Resolution resolution) {
        this.resolution = resolution;
    }

    @Override
    public List<Image.ImageInfo> getWithAction(Consumer<Image.ImageInfo> action, List<Integer> indexes) {
        Map<Integer, List<Integer>> map = parseIndexes(indexes);
        LinkedList<Image.ImageInfo> list = new LinkedList<>();

        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            int idx = 0;
            try {
                LOGGER.info("Fetching {} ...", getUrl(entry.getKey()));
                Document document = Jsoup.connect(getUrl(entry.getKey())).get();
                for (Element element : select(document)) {
                    if (!entry.getValue().contains(idx++)) continue;
                    list.add(parse(element));
                    if (action != null) action.accept(list.getLast());
                }
            } catch (Exception e) {
                LOGGER.error("Cannot get the page of index {}", entry.getKey(), e);
            }
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
}
