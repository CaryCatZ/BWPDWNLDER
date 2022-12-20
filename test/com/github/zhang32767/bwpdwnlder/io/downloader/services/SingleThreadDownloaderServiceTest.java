package com.github.zhang32767.bwpdwnlder.io.downloader.services;

import com.github.zhang32767.bwpdwnlder.io.downloader.SingleThreadDownloader;
import com.github.zhang32767.bwpdwnlder.main.Main;
import org.junit.Before;

public class SingleThreadDownloaderServiceTest extends DownloaderServiceTest<SingleThreadDownloader> {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        service = new SingleThreadDownloaderService(null, new SingleThreadDownloader());
    }
}