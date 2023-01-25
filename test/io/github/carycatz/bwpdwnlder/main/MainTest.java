package io.github.carycatz.bwpdwnlder.main;


import io.github.carycatz.bwpdwnlder.test.Super;
import org.junit.Test;

import java.util.stream.IntStream;

public class MainTest extends Super {
    @Test
    public void main() throws Exception {
        // Test with arguments
        StringBuilder string1 = new StringBuilder();
        IntStream.range(1, 16).forEach(i -> string1.append(i).append(" "));
        string1.append("--path test/temp --format {date}.jpg");
        String[] s1 = string1.toString().split(" ");
        new Thread(() -> Main.main(s1)).start();

        waitUntilStopping(Main.executor);
        assertDownloaded();

        super.cleanDir();
    }
}