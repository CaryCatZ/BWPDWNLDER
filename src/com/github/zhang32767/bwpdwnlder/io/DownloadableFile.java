package com.github.zhang32767.bwpdwnlder.io;


import java.io.File;

public class DownloadableFile extends File implements Downloadable, LockableFile {
    private final String address;

    public DownloadableFile(String pathname, String address) {
        super(pathname);
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }
}
