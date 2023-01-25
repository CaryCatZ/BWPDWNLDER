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
        string1.append("--path test/temp --format {index}.jpg");
        String[] s1 = string1.toString().split(" ");
        Main.main(s1);

        assertDownloaded();

        super.cleanDir();

        // Test without arguments
        String[] s2 = "--path test/temp --format {index}.jpg".split(" ");
        Main.main(s2);

        assertDownloaded();
    }
}