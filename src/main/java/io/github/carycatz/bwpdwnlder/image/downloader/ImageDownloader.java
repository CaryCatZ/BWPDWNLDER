package io.github.carycatz.bwpdwnlder.image.downloader;

import io.github.carycatz.bwpdwnlder.image.sources.Source;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.nio.file.Path;
import java.util.List;

public interface ImageDownloader {
    static ImageDownloader create(Source source, Downloader downloader, Path path, String format) {
        return new ImageDownloaderImpl(source, downloader, path, format);
    }

    default void download(int index) {
        download(index, null);
    }

    default void download(List<Integer> indexes) {
        download(indexes, null);
    }

    void download(int index, Runnable reporterHook);

    void download(List<Integer> indexes, Runnable reporterHook);
}
