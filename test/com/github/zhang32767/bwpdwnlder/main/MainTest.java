package com.github.zhang32767.bwpdwnlder.main;


import com.github.zhang32767.bwpdwnlder.test.AutoSetUp;
import org.junit.Test;

public class MainTest extends AutoSetUp {
    @Test
    public void main() throws Exception {
        String[] s = "1 2 3 4 5 6 7 8 --path test/temp --format {index}.jpg".split(" ");
        Main.main(s);

        waitFor();
        assertDownloaded();
    }
}