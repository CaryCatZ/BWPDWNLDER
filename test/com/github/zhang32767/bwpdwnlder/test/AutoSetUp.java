package com.github.zhang32767.bwpdwnlder.test;


import com.github.zhang32767.bwpdwnlder.main.Main;
import org.junit.Before;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public abstract class AutoSetUp {
    @Before
     public void setUp() throws Exception {
        try (Stream<Path> stream = Files.list(Path.of("test/temp"))) {
            for (Path path : stream.toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    protected void waitFor() throws Exception {
        TimeUnit.SECONDS.sleep(10);
        while (!Main.getExecutor().getQueue().isEmpty() || Main.getExecutor().getActiveCount() != 0) {
            Thread.yield();
        }
    }

    protected void assertDownloaded() throws Exception {
        Path path;
        for (int i = 0; i < 8; i++) {
            path = Path.of("test/temp/%s.jpg".formatted(i+1));
            assertTrue(Files.exists(path));
            try (InputStream in = new FileInputStream(path.toFile())) {
                assertTrue(in.available() != 0);
            }
        }
    }
}
