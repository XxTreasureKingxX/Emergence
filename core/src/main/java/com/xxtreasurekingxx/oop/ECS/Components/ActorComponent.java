package com.xxtreasurekingxx.oop.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

public class ActorComponent implements Pool.Poolable, Component {
    public Actor actor;
    public boolean needsActor;
    public boolean needsDelete;

    @Override
    public void reset() {
        actor = null;
        needsActor = false;
        needsDelete = false;
    }
}
