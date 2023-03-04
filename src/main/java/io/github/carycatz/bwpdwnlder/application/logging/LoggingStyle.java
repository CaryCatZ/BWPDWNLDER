/*
 * Copyright (c) 2023 CaryCatZ<carycatz@outlook.com>
 * Licensed under the MIT License. See License in the project root for license information.
 */

package io.github.carycatz.bwpdwnlder.application.logging;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("unused")
public enum LoggingStyle {
    LESS("/META-INF/log4j2-less.xml"),
    FULL("/META-INF/log4j2-full.xml");

    public final URI uri;

    LoggingStyle(String path) {
        try {
            //noinspection DataFlowIssue
            uri = LoggingStyle.class.getResource(path).toURI();
        } catch (URISyntaxException e) { //shouldn't happen
            throw new AssertionError(e);
        }
    }
}
