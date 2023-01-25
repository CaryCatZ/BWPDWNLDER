package io.github.carycatz.bwpdwnlder.io;


import java.io.File;

public class DownloadableFile extends File implements Downloadable, LockableFile {
    protected String url;

    public DownloadableFile(String pathname, String url) {
        super(pathname);
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
