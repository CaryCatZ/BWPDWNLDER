/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.io;


import java.io.File;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && obj instanceof DownloadableFile downloadableFile
                && Objects.equals(downloadableFile.url, this.url);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 31 * url.hashCode();
    }
}
