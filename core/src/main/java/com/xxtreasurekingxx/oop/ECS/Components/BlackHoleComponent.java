package com.xxtreasurekingxx.oop.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class BlackHoleComponent implements Pool.Poolable, Component {
    public float coolDown;
    public float coolDownTimer;

    @Override
    public void reset() {
        coolDown = 0;
        coolDownTimer = 0;
    }
}
