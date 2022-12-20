package com.github.zhang32767.bwpdwnlder.main;


import com.github.zhang32767.bwpdwnlder.test.Super;
import org.junit.Test;

public class MainTest extends Super {
    @Test
    public void main() throws Exception {
        String[] s = "1 2 3 4 5 6 7 8 --path test/temp --format {index}.jpg".split(" ");
        Main.main(s);
  
        waitUntilStopping(Main.getExecutor());
        assertDownloaded();
    }
}