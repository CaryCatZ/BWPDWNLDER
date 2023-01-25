package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.Resolution;
import io.github.carycatz.bwpdwnlder.main.Main;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

/**
 * <p>Thanks to the author of bing.ioliu.cn</p>
 * <p>We download image info from bing.ioliu.cn</p>
 * <p><a href="https://bing.ioliu.cn">Site</a> <a href="github.com/xCss/bing">Github</p>
 **/
class IoliuSource extends AbstractSource {
    public static final String URL = "https://bing.ioliu.cn/?p=%s";

    IoliuSource(Resolution resolution) {
        super(resolution);
    }

    @Override
    public Image.ImageInfo get(int index) {
        return gets(List.of(index)).get(0);
    }

    @Override
    public List<Image.ImageInfo> gets(List<Integer> indexes) {
        Map<Integer, List<Integer>> map = parseIndexes(indexes);
        List<Image.ImageInfo> list = new LinkedList<>();

        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            try {
                Document document = Jsoup.connect(URL.formatted(entry.getKey())).get();
                for (Element item : document.selectXpath("//*[@class='item']")) {
                    String url = Objects.requireNonNull(item.selectXpath("//*[text() = '%s']".formatted(resolution)).get(0).parent()).attr("href");
                    String description = item.selectXpath("//*[@class='description']/h3").get(0).text();
                    String name = url.split("=")[1].split("&")[0].replace("_" + resolution + ".jpg", "");
                    String date = item.selectXpath("//*[@class='calendar']/em").get(0).text();
                    list.add(new Image.ImageInfo(url, description, name, date, resolution));
                }
            } catch (Exception e) {
                Main.LOGGER.error("Cannot get the page of index {}", entry.getKey(), e);
            }
        }
        return list;
    }

    private Pair<Integer, Integer> parseIndex(int index) {
        int idx = index % 11;
        int page = (index - idx) / 11 + 1;
        return Pair.of(page, idx);
    }

    private Map<Integer, List<Integer>> parseIndexes(List<Integer> indexes) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        indexes.forEach(i -> {
            Pair<Integer, Integer> pair = parseIndex(i);
            map.putIfAbsent(pair.getLeft(), new LinkedList<>());
            map.get(pair.getLeft()).add(pair.getRight());
        });
        return map;
    }
}
