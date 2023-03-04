/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.features.image.shell;

import io.github.carycatz.bwpdwnlder.features.image.Image;
import io.github.carycatz.bwpdwnlder.features.image.downloading.ImageDownloaderImpl;
import io.github.carycatz.bwpdwnlder.features.image.source.Source;
import io.github.carycatz.bwpdwnlder.features.image.source.SourceFactory;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class SessionImageDownloader extends ImageDownloaderImpl implements AutoCloseable {
    private static final Map<Integer, SessionImageDownloader> SESSIONS = new ConcurrentHashMap<>();

    public final int sessionId;

    public static SessionImageDownloader getSession(int id) {
        return SESSIONS.get(id);
    }

    private SessionImageDownloader(Source source, Downloader downloader, String format) {
        super(source, downloader, format);
        this.sessionId = Objects.hash(source, downloader, format);
        SESSIONS.put(sessionId, this);
    }

    public void download(int index, File path, String format, Runnable reporterHook) {
        download(source.get(index), path, format, reporterHook);
    }

    public void download(List<Integer> indexes, File path, String format, Runnable reporterHook) {
        source.getWithAction(info -> download(info, path, format, reporterHook), indexes);
    }

    @Override
    public void close() {
        SESSIONS.remove(this.sessionId);
    }

    public static final class SessionBuilder {
        public static SessionBuilder newBuilder() {
            return new SessionBuilder();
        }

        private Source source;
        private Downloader downloader;
        private String format = "";

        private SessionBuilder() {
        }

        public SessionBuilder setSource(Source source) {
            this.source = source;
            return this;
        }

        public SessionBuilder setDownloader(Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        public SessionBuilder setFormat(String format) {
            this.format = format;
            return this;
        }

        public SessionImageDownloader build() {
            Source source = this.source == null ? SourceFactory.defaultSource() : this.source;
            Downloader downloader = this.downloader;
            String format = this.format.isEmpty() ? Image.DEFAULT_FORMAT : this.format;
            return new SessionImageDownloader(source, downloader, format);
        }
    }
}
