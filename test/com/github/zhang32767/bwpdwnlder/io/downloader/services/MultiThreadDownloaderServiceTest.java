package com.github.zhang32767.bwpdwnlder.io.downloader.services;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;
import com.github.zhang32767.bwpdwnlder.io.downloader.MultiThreadDownloader;
import com.github.zhang32767.bwpdwnlder.main.Main;
import com.github.zhang32767.bwpdwnlder.test.AutoSetUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultiThreadDownloaderServiceTest extends AutoSetUp {
    private MultiThreadDownloaderService service;
    @Before
    public void setUp() throws Exception {
        service = new MultiThreadDownloaderService(Main.getExecutor(), new MultiThreadDownloader(Main.getExecutor()));
        super.setUp();
    }

    @After
    public void tearDown() {
        service = null;
    }

    @Test
    public void download() throws Exception {
        String[] urls = new String[]{
                "https://cn.bing.com/th?id=OHR.WinterberryBush_ZH-CN1414026440_UHD.jpg&amp;qlt=100",
                "https://cn.bing.com/th?id=OHR.SouthBeach_ZH-CN0989287734_UHD.jpg&amp;qlt=100",
                "https://cn.bing.com/th?id=OHR.GlacierGoats_ZH-CN0764810245_UHD.jpg&qlt=100",
                "https://cn.bing.com/th?id=OHR.DudhsagarFallsGoa_ZH-CN0466471017_UHD.jpg&qlt=100",
                "https://cn.bing.com/th?id=OHR.Borovets_ZH-CN5914681811_UHD.jpg&amp;qlt=100",
                "https://cn.bing.com/th?id=OHR.GranParadiso100th_ZH-CN5744961532_UHD.jpg&qlt=100",
                "https://cn.bing.com/th?id=OHR.PoinsettiaDay_ZH-CN5115071992_UHD.jpg&qlt=100",
                "https://cn.bing.com/th?id=OHR.InstagramHallstatt_ZH-CN5309282641_UHD.jpg&qlt=100"
        };

        for (int i = 0; i < 8; i++) {
            DownloadableFile file = new DownloadableFile("C:/Users/Zhang/javaProjects/BWPDWNLDER/test/temp/%s.jpg".formatted(i+1), urls[i]);
            service.download(file);
        }

        waitFor();
        assertDownloaded();
    }
}