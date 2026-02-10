package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.xxtreasurekingxx.oop.World.WorldContactListener;

import static com.xxtreasurekingxx.oop.Core.FPS;

public class PhysicsSystem extends EntitySystem {

    private final World world;
    private final WorldContactListener contactListener;
    private float accumulator;

    public PhysicsSystem() {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(contactListener = new WorldContactListener());
    }

    @Override
    public void update(float delta) {
        accumulator += delta;
        accumulator = Math.min(accumulator, 0.25f);

        while (accumulator >= FPS) {
            world.step(FPS, 6, 2);
            accumulator -= FPS;
        }
    }

    public World getWorld() {
        return world;
    }

    public WorldContactListener getContactListener() {
        return contactListener;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        world.dispose();
    }
}
