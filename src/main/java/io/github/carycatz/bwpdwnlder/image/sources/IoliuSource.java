package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Image;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;

/**
 * <p>Thanks to the author of bing.ioliu.cn</p>
 * <p>We download image info from bing.ioliu.cn</p>
 * <p><a href="https://bing.ioliu.cn">Site</a> <a href="github.com/xCss/bing">Github</p>
 **/
public final class IoliuSource extends AbstractSource {
    public static final String URL = "https://bing.ioliu.cn/?p=%s";

    IoliuSource(Image.Resolution resolution) {
        super(resolution);
    }

    @Override
    protected Elements select(Document document) {
        return document.selectXpath("//*[@class='item']");
    }

    @Override
    protected String getUrl(int page) {
        return URL.formatted(page);
    }

    @Override
    protected Image.ImageInfo parse(Element element) {
        String url = Objects.requireNonNull(element.selectXpath(".//*[text() = '%s']".formatted(resolution)).get(0).parent()).attr("href").split("&")[0].replace("https://bing.com", BING);
        String description = element.selectXpath(".//*[@class='description']/h3").get(0).text();
        String name = url.split("=")[1].replace("_" + resolution + ".jpg", "");
        String date = element.selectXpath(".//*[@class='calendar']/em").get(0).text();
        return new Image.ImageInfo(url, description, name, date, resolution);
    }

    @Override
    protected int imageOfEachPage() {
        return 12;
    }
}
