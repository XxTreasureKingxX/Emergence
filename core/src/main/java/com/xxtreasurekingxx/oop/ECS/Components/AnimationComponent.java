package com.xxtreasurekingxx.oop.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.oop.World.AnimationType;

public class AnimationComponent implements Pool.Poolable, Component {
    public AnimationType type;
    public float animationTimer;

    @Override
    public void reset() {
        type = null;
        animationTimer = 0;
    }
}
