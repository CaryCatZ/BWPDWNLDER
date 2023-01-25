package io.github.carycatz.bwpdwnlder.image;

public enum Resolution {
    R_UHD(-1, -1), // unknown weight and height but it is the highest
    R_1080P(1920, 1080),
    R_720P(1280, 780),
    R_480(400, 480);

    public final int weight;
    public final int height;
    Resolution(int weight, int height) {
        this.weight = weight;
        this.height = height;
    }

    @Override
    public String toString() {
        return this == R_UHD ? "UHD" : weight + "x" + height;
    }
}
