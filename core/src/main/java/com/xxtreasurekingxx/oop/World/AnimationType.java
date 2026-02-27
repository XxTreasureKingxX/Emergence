package com.xxtreasurekingxx.oop.World;

public enum AnimationType {
    SUN("objects.atlas", "objects", 1f, 1, 128),
    JUPITER("objects.atlas", "objects", 1f, 1, 112),
    EARTH("objects.atlas", "objects", 1f, 2, 80),
    SATURN("objects.atlas", "objects", 1f, 3, 72),
    HOLES("objects.atlas", "objects", 1f, 4, 64),
    PASTEL("objects.atlas", "objects", 1f, 5, 56),
    GREEN("objects.atlas", "objects", 1f, 6, 48),
    BLUSS("objects.atlas", "objects", 1f, 7, 40),
    TODO("objects.atlas", "objects", 1f, 8, 32),
    HOLE("objects.atlas", "objects", 0.1f, 9, 32),
    ANOMALY("objects.atlas", "objects", 1f, 10, 28),
    GOLD("objects.atlas", "objects", 1f, 11, 28),
    RING("ring.atlas", "ring", 0.1f, 0, 128);

    /*new names:::::::: 32 40 48 56 64 72 80 88 96 104 112
    BLUSS
    TOD O
    * */

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
