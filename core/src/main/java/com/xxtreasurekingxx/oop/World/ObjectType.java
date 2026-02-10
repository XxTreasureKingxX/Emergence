package com.xxtreasurekingxx.oop.World;

public enum ObjectType {

    SUN(AnimationType.SUN, 0.9f, null, 999999),
    JUPITER(AnimationType.JUPITER, 0.8f, SUN, 2560),
    SATURN(AnimationType.SATURN, 0.7f, JUPITER, 1280),
    URANUS(AnimationType.URANUS, 0.6f, SATURN, 640),
    NEPTUNE(AnimationType.NEPTUNE, 0.5f, URANUS, 320),
    EARTH(AnimationType.EARTH, 0.4f, NEPTUNE, 160),
    VENUS(AnimationType.VENUS, 0.3f, EARTH, 80),
    MARS(AnimationType.MARS, 0.2f, VENUS, 40),
    MERCURY(AnimationType.MERCURY, 0.1f, MARS, 20),
    HOLE(AnimationType.HOLE, 100, null, 999999),
    ANOMALY(AnimationType.ANOMALY, 5f, null, 999999);

    final AnimationType animationType;
    final float density;
    final ObjectType upgrade;
    final int upgradeThreshold;

    ObjectType(final AnimationType animationType, final float density, final ObjectType upgrade, final int upgradeThreshold) {
        this.animationType = animationType;
        this.density = density;
        this.upgrade = upgrade;
        this.upgradeThreshold = upgradeThreshold;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public float getDensity() {
        return density;
    }

    public ObjectType getUpgrade() {
        return upgrade;
    }

    public int getUpgradeThreshold() {
        return upgradeThreshold;
    }
}
