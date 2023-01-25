package io.github.carycatz.bwpdwnlder.io.downloader.services;

import io.github.carycatz.bwpdwnlder.io.downloader.SingleThreadDownloader;
import org.junit.Before;

public class SingleThreadDownloaderServiceTest extends DownloaderServiceTest<SingleThreadDownloader> {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        service = new SingleThreadDownloaderService(null, new SingleThreadDownloader());
    }
}