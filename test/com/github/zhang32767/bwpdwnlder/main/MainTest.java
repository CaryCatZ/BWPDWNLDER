package com.github.zhang32767.bwpdwnlder.main;


import com.github.zhang32767.bwpdwnlder.test.Super;
import org.junit.Test;

import java.util.stream.IntStream;

public class MainTest extends Super {
    @Test
    public void main() throws Exception {
        StringBuilder string = new StringBuilder();
        IntStream.range(1, 16).forEach(i -> string.append(i).append(" "));
        string.append("--path test/temp --format {index}.jpg");
        String[] s = string.toString().split(" ");
        Main.main(s);

        waitUntilStopping(Main.getExecutor());
        assertDownloaded();
    }
}