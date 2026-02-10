package com.xxtreasurekingxx.oop.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.oop.World.ObjectType;

public class ObjectComponent implements Pool.Poolable, Component {
    public ObjectType type;
    public int exp;

    @Override
    public void reset() {
        type = null;
        exp = 0;
    }
}
