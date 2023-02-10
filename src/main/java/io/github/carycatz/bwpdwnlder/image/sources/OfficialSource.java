package io.github.carycatz.bwpdwnlder.image.sources;

import com.google.gson.JsonObject;
import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.main.Main;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static io.github.carycatz.bwpdwnlder.main.Main.GSON;
import static io.github.carycatz.bwpdwnlder.main.Main.LOGGER;

public final class OfficialSource extends AbstractSource {
    public static final String URL = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=%s&n=1&mkt=zh-CN";

    OfficialSource(Image.Resolution resolution) {
        super(resolution);
    }

    @Override
    public Image.ImageInfo get(int index) {
        if (index >= 15) {
            Main.LOGGER.warn("Got invalid index: {} available range: 0 - 15", index);
            return null;
        }
        try {
            JsonObject json = GSON.fromJson(connect(URL.formatted(index)), JsonObject.class).get("images").getAsJsonArray().get(0).getAsJsonObject();
            String url = BING + json.get("urlbase").getAsString() + "_%s.jpg".formatted(resolution);
            String description = json.get("copyright").getAsString();
            String name = json.get("urlbase").getAsString().split("=")[1];
            String date = LocalDateTime.now().plusDays(-index).format(DateTimeFormatter.ISO_LOCAL_DATE);
            return new Image.ImageInfo(url, description, name, date, resolution);
        } catch (Exception e) {
            LOGGER.error("Cannot get the image of index {}", index, e);
            return null;
        }
    }

    @Override
    public List<Image.ImageInfo> getWithAction(Consumer<Image.ImageInfo> action, List<Integer> indexes) {
        LinkedList<Image.ImageInfo> list = new LinkedList<>();
        indexes.forEach(i -> {
            list.add(get(i));
            action.accept(list.getLast());
        });
        return list;
    }

    private String connect(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setDoInput(true);
            connection.connect();
            return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(connection.getInputStream().readAllBytes())).toString();
        } catch (MalformedURLException e) {
            LOGGER.warn("Got invalid url: {}", url);
            return null;
        } catch (Exception e) {
            LOGGER.error("Cannot connect to {}", url, e);
            return null;
        }
    }
}
