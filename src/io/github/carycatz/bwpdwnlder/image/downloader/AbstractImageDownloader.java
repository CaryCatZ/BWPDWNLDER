package io.github.carycatz.bwpdwnlder.image.downloader;

import io.github.carycatz.bwpdwnlder.image.Image;
import io.github.carycatz.bwpdwnlder.image.sources.Source;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.nio.file.Path;

public abstract class AbstractImageDownloader implements ImageDownloader {
    protected final Source source;
    protected final Downloader downloader;
    protected final Path path;
    protected final String format;

    AbstractImageDownloader(Source source, Downloader downloader, Path path, String format) {
        this.source = source;
        this.downloader = downloader;
        this.path = path;
        this.format = format;
    }

    protected void download(Image.ImageInfo info, Runnable reporterHook) {
        downloader.download(new Image(info, path.toString(), info.format(format)), reporterHook);
    }
}
