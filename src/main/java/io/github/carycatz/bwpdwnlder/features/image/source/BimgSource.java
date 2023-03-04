/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.source;

import io.github.carycatz.bwpdwnlder.features.image.Image;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static io.github.carycatz.bwpdwnlder.features.image.source.SourceRuntime.BING;

/**
 * <a href="https://bimg.top/category">Site</a>
 **/
public final class BimgSource extends GenericSource {
    public static final String URL = "http://bimg.top/category/wallpaper/page/%s";

    BimgSource(Image.Resolution resolution) {
        super(resolution);
    }

    @Override
    protected Elements select(Document document) {
        return document.selectXpath("//div[@class='entry-thumb']");
    }

    @Override
    protected String getUrl(int page) {
        return URL.formatted(page);
    }

    @Override
    protected Image.ImageInfo parse(Element element) {
        String[] s = element.selectXpath(".//a").attr("href").split("/");
        String url = BING + "/th?id=" + s[5].split("!")[0];
        String description = element.selectXpath(".//a").attr("data-title");
        String name = url.split("=")[1].split("&")[0].replace("_" + resolution.getString() + ".jpg", "");
        String date = s[4];
        return new Image.ImageInfo(url, description, name, date, resolution);
    }

    @Override
    protected int imageOfEachPage() {
        return 31;
    }
}
