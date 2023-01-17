package io.github.carycatz.bwpdwnlder.io.downloader.services;

import io.github.carycatz.bwpdwnlder.io.downloader.MultiThreadDownloader;
import org.junit.Before;

public class MultiThreadDownloaderServiceTest extends DownloaderServiceTest<MultiThreadDownloader> {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        service = new MultiThreadDownloaderService(executor, new MultiThreadDownloader(executor));
    }
}