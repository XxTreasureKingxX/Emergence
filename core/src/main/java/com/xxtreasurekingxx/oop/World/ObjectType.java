package com.xxtreasurekingxx.oop.World;

import static com.xxtreasurekingxx.oop.Core.baseExp;

public enum ObjectType {

    SUN(AnimationType.SUN, 0.9f, null, 1000000),
    JUPITER(AnimationType.JUPITER, 0.8f, SUN, baseExp*256),
    SATURN(AnimationType.SATURN, 0.7f, JUPITER, baseExp*128),
    URANUS(AnimationType.URANUS, 0.6f, SATURN, baseExp*64),
    NEPTUNE(AnimationType.NEPTUNE, 0.5f, URANUS, baseExp*32),
    EARTH(AnimationType.EARTH, 0.4f, NEPTUNE, baseExp*16),
    VENUS(AnimationType.VENUS, 0.3f, EARTH, baseExp*8),
    MARS(AnimationType.MARS, 0.2f, VENUS, baseExp*4),
    MERCURY(AnimationType.MERCURY, 0.1f, MARS, baseExp*2),
    HOLE(AnimationType.HOLE, 100, null, 1000000),
    ANOMALY(AnimationType.ANOMALY, 5f, null, 1000000);

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
