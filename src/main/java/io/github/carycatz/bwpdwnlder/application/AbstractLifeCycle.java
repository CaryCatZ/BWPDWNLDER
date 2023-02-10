package io.github.carycatz.bwpdwnlder.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.carycatz.bwpdwnlder.application.main.Main;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractLifeCycle {
    protected static ThreadPoolExecutor executor;
    protected static Downloader downloader;
    protected static Main.Args args;
    public static final Logger LOGGER = LogManager.getLogger("BWPDWNLDER");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static boolean IS_SINGLE_THREAD_MODE = false;
}
