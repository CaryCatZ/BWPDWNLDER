package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.Resolution;
import io.github.carycatz.bwpdwnlder.main.Main;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>Thanks to the author of Bimg.top</p>
 * <p>We download images from bimg.top</p>
 * <p><a href="https://bimg.top/category">Site</a><p>
 **/
class BimgSource extends AbstractSource {
    public static final String URL = "http://bimg.top/category/wallpaper/page/%s";
    public static final String BING = "https://cn.bing.com";
    public static final Pattern PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})/(.*?)(?=!webp)");

    BimgSource(Resolution resolution) {
        super(resolution);
    }

    @Override
    public Image.ImageInfo get(int index) {
        return null;
    }

    @Override
    public List<Image.ImageInfo> gets(List<Integer> indexes) {
        Map<Integer, List<Integer>> map = parseIndexes(indexes);
        List<Image.ImageInfo> list = new LinkedList<>();

        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            try {
                Document document = Jsoup.connect(URL.formatted(entry.getKey())).get();
                for (Element item : document.selectXpath("//[@class='site-main']//div[@class='entry-thumb']")) {
                    String[] s = PATTERN.matcher(item.selectXpath("//a").attr("href")).group().split("/");
                    String url = BING + "/th?id=" + s[1];
                    String description = item.selectXpath("//a").attr("data-title");
                    String name = url.split("=")[1].split("&")[0].replace("_" + resolution + ".jpg", "");
                    String date = s[0];
                    list.add(new Image.ImageInfo(url, description, name, date, resolution));
                }
            } catch (Exception e) {
                Main.LOGGER.error("Cannot get the page of index {}", entry.getKey(), e);
            }
        }
        return list;
    }

    private Pair<Integer, Integer> parseIndex(int index) {
        int idx = index % 30;
        int page = (index - idx) / 30 + 1;
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
