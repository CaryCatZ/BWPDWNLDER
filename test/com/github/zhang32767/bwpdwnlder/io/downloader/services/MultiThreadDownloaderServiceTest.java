package com.github.zhang32767.bwpdwnlder.io.downloader.services;

import com.github.zhang32767.bwpdwnlder.io.downloader.MultiThreadDownloader;
import org.junit.Before;

public class MultiThreadDownloaderServiceTest extends DownloaderServiceTest<MultiThreadDownloader> {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        service = new MultiThreadDownloaderService(executor, new MultiThreadDownloader(executor));
    }
}