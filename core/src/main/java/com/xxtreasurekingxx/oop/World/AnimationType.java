package com.xxtreasurekingxx.oop.World;

public enum AnimationType {
    SUN("objects.atlas", "objects", 1f, 0, 128),
    JUPITER("objects.atlas", "objects", 1f, 1, 112),
    SATURN("objects.atlas", "objects", 1f, 2, 96),
    URANUS("objects.atlas", "objects", 1f, 3, 72),
    NEPTUNE("objects.atlas", "objects", 1f, 4, 64),
    EARTH("objects.atlas", "objects", 1f, 5, 56),
    VENUS("objects.atlas", "objects", 1f, 6, 48),
    MARS("objects.atlas", "objects", 1f, 7, 40),
    MERCURY("objects.atlas", "objects", 1f, 8, 32),
    HOLE("objects.atlas", "objects", 0.1f, 9, 32),
    ANOMALY("objects.atlas", "objects", 1f, 10, 28),
    RING("ring.atlas", "ring", 0.1f, 0, 128);

    final String atlas;
    final String region;
    final float frameRate;
    final int row;
    final int diameter;

    AnimationType(final String atlas, final String region, final float frameRate, final int row, final int diameter) {
        this.atlas = atlas;
        this.region = region;
        this.frameRate = frameRate;
        this.row = row;
        this.diameter = diameter;
    }

    public String getAtlas() {
        return atlas;
    }

    public String getRegion() {
        return region;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public int getRow() {
        return row;
    }

    public int getDiameter() {
        return diameter;
    }
}
