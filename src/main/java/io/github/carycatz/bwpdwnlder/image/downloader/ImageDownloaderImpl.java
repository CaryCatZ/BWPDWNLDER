package io.github.carycatz.bwpdwnlder.image.downloader;

import io.github.carycatz.bwpdwnlder.image.sources.Source;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.nio.file.Path;
import java.util.List;

class ImageDownloaderImpl extends AbstractImageDownloader {
    ImageDownloaderImpl(Source source, Downloader service, Path path, String format) {
        super(source, service, path, format);
    }

    @Override
    public void download(int index, Runnable reporterHook) {
        download(source.get(index), reporterHook);
    }

    @Override
    public void download(List<Integer> indexes, Runnable reporterHook) {
        source.getWithAction(info -> download(info, reporterHook), indexes);
    }
}
