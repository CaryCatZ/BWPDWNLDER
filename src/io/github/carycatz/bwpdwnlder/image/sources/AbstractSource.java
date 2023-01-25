package io.github.carycatz.bwpdwnlder.image.sources;

import io.github.carycatz.bwpdwnlder.image.Resolution;

public abstract class AbstractSource implements Source {
    public final Resolution resolution;

    protected AbstractSource() {
        this(Resolution.R_UHD);
    }

    protected AbstractSource(Resolution resolution) {
        this.resolution = resolution;
    }
}
