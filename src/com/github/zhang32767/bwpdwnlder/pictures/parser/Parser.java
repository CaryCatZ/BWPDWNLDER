package com.github.zhang32767.bwpdwnlder.pictures.parser;

import com.github.zhang32767.bwpdwnlder.pictures.Picture;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;

public class Parser {
    private Parser() {
    }

    public static LinkedList<Picture.PictureInfo> parse(Document doc) {
        return parse(doc, List.of(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    }

    public static LinkedList<Picture.PictureInfo> parse(Document doc, List<Integer> indexes) {
        LinkedList<Picture.PictureInfo> list = new LinkedList<>();

        int index = 0;
        for (Element e : doc.selectXpath("//*[text()='UHD']")) {
            if (e.parent() != null && indexes.contains(index)) {
                Element element = e.selectXpath("../../../div[@class='description']").get(0);
                String url = e.parent().attr("href").replaceFirst("bing\\.com", "cn.bing.com");
                String description = element.selectXpath("h3").text();
                String date = element.selectXpath("p[@class='calendar']/em").text();
                list.add(new Picture.PictureInfo(url, description, date, index));
            }
            index++;
        }

        return list;
    }
}
