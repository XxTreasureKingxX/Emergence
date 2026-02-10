package com.xxtreasurekingxx.oop.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class B2DComponent implements Pool.Poolable, Component {
    public float width;
    public float height;
    public Body body;
    public Vector2 renderPosition;
    public boolean needsBody;
    public boolean needsSquareBody;
    public boolean needsDelete;
    public boolean isBorder;
    public boolean shouldCollide;
    public float angle;

    @Override
    public void reset() {
        if(body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
        width = 0;
        height = 0;
        renderPosition = new Vector2(0, 0);
        needsBody = false;
        needsSquareBody = false;
        needsDelete = false;
        isBorder = false;
        angle = 0;
        shouldCollide = true;
    }
}
