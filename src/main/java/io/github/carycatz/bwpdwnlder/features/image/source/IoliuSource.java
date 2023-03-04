/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.source;

import io.github.carycatz.bwpdwnlder.features.image.Image;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;

import static io.github.carycatz.bwpdwnlder.features.image.source.SourceRuntime.BING;

/**
 * <a href="https://bing.ioliu.cn">Site</a><a href="https://github.com/xCss/bing">Github</a>
 **/
public final class IoliuSource extends GenericSource {
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
        String url = Objects.requireNonNull(
                element.selectXpath(".//*[text() = '1920x1080']").get(0).parent()
        ).attr("href").split("&")[0]
                .replace("1920x1080", resolution.getString()).replace("https://bing.com", BING);
        String description = element.selectXpath(".//*[@class='description']/h3").get(0).text();
        String name = url.split("=")[1].replace("_" + resolution.getString() + ".jpg", "");
        String date = element.selectXpath(".//*[@class='calendar']/em").get(0).text();
        return new Image.ImageInfo(url, description, name, date, resolution);
    }

    @Override
    protected int imageOfEachPage() {
        return 12;
    }
}
