package io.github.carycatz.bwpdwnlder.test;


import org.junit.After;
import org.junit.Before;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public abstract class Super {
    @Before
     public void setUp() throws Exception {
        cleanDir();
    }

    @After
    public void tearDown() throws Exception {
//        cleanDir();
    }

    protected void cleanDir() throws Exception {
        try (Stream<Path> stream = Files.list(Path.of("test/temp"))) {
            for (Path path : stream.toList()) {
                if (!Files.isDirectory(path)) {
                    Files.deleteIfExists(path);
                }
            }
        }
    }

    protected void waitUntilStopping(ThreadPoolExecutor executor) throws Exception {
        TimeUnit.SECONDS.sleep(5);
        while (!executor.getQueue().isEmpty() || executor.getActiveCount() != 0) {
            Thread.onSpinWait();
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
