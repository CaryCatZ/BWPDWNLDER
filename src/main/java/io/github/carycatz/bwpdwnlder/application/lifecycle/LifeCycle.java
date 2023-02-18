package io.github.carycatz.bwpdwnlder.application.lifecycle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.carycatz.bwpdwnlder.application.lifecycle.logging.LoggingUtil;
import io.github.carycatz.bwpdwnlder.application.main.Arguments;
import io.github.carycatz.bwpdwnlder.io.downloader.Downloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadPoolExecutor;

public class LifeCycle {
    protected static ThreadPoolExecutor executor;
    protected static Downloader downloader;
    protected static Arguments arguments = new Arguments();
    protected static boolean IS_SINGLE_THREAD_MODE = false;
    public static final Logger LOGGER = LogManager.getLogger("BWPDWNLDER");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    static {
        LoggingUtil.reconfigure(arguments.logLvl);
    }

    protected LifeCycle() {
        if (this.getClass() == LifeCycle.class) {
            throw new InstantiationError();
        }
    }
}
