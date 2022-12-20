package com.github.zhang32767.bwpdwnlder.io.downloader.services;

import com.github.zhang32767.bwpdwnlder.io.DownloadableFile;
import com.github.zhang32767.bwpdwnlder.io.downloader.Downloader;
import com.github.zhang32767.bwpdwnlder.test.Super;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class DownloaderServiceTest<T extends Downloader> extends Super {
    protected ThreadPoolExecutor executor;
    protected AbstractDownloaderService<T> service;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        executor = new ThreadPoolExecutor(12, 24, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(64));
    }

    @After
    public void tearDown() throws Exception{
        super.tearDown();
        service = null;
        executor.shutdown();
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
            DownloadableFile file = new DownloadableFile("test/temp/%s.jpg".formatted(i+1), urls[i]);
            service.download(file);
        }

        waitUntilStopping(executor);
        assertDownloaded();
    }
}
