package io.github.carycatz.bwpdwnlder.application.lifecycle.logging;

import com.google.common.io.Files;
import io.github.carycatz.bwpdwnlder.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.spi.StandardLevel;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static io.github.carycatz.bwpdwnlder.application.lifecycle.LifeCycle.LOGGER;

public final class LoggingUtil {
    public static final URI DEFAULT_LOGGING_CONFIG;
    public static final URI CUSTOM_LOGGING_CONFIG;
    private static final String TEMP_FILE_PREFIX = Application.APPLICATION_NAME + "-LOG4J-CONFIG-";

    static {
        try {
            DEFAULT_LOGGING_CONFIG = Objects.requireNonNull(
                    LoggingUtil.class.getResource("/META-INF/log4j2-default.xml")
            ).toURI();
            CUSTOM_LOGGING_CONFIG = Objects.requireNonNull(
                    LoggingUtil.class.getResource("/META-INF/log4j2-custom.xml")
            ).toURI();
        } catch (URISyntaxException e) { //shouldn't happen
            throw new Error(e);
        }
    }

    public static void reconfigure(StandardLevel lvl) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        try (InputStream in = CUSTOM_LOGGING_CONFIG.toURL().openStream()) {
            File file = File.createTempFile(TEMP_FILE_PREFIX, ".xml");
            Files.asCharSink(file, StandardCharsets.UTF_8).write(
                    StandardCharsets.UTF_8.decode(ByteBuffer.wrap(in.readAllBytes()))
                            .toString().replace("${log-level}", lvl.name())
            );
            context.setConfigLocation(file.toURI());
        } catch (Exception e) { // fallback
            LOGGER.warn("Cannot reconfigure", e);
            context.setConfigLocation(DEFAULT_LOGGING_CONFIG);
        }
    }

    private LoggingUtil() {
        throw new InstantiationError();
    }
}
