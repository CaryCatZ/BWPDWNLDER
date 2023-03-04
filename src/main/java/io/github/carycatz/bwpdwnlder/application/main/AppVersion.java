/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.main;

import io.github.carycatz.bwpdwnlder.application.runtime.ApplicationRuntime;
import io.github.carycatz.bwpdwnlder.misc.LazyInitializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

public final class AppVersion {
    public static final String VERSION_FILE_PATH = "/META-INF/version.properties";
    private static final Supplier<AppVersion> SUPPLIER = (LazyInitializer<AppVersion>) () -> {
        //noinspection DataFlowIssue
        try (InputStream in = AppVersion.class.getResource(VERSION_FILE_PATH).openStream()) {
            Properties properties = new Properties(2);
            properties.load(in);
            String version = (String) properties.get("version");
            String commit = (String) properties.get("commit");
            String date = (String) properties.get("date");
            return new AppVersion(version, commit, date);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public final String version;
    public final String commit;
    public final String date;

    private AppVersion(String version, String commit, String date) {
        this.version = version;
        this.commit = commit;
        this.date = date;
    }

    public static AppVersion getInstance() {
        return SUPPLIER.get();
    }

    @Override
    public String toString() {
        return "%s %s (tags/%s:%s, %s)"
                .formatted(
                        ApplicationRuntime.APPLICATION_NAME,
                        version,
                        version,
                        commit,
                        date
                        );
    }
}
