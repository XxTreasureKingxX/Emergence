package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.ObjectType;

import java.util.HashMap;

import static com.badlogic.gdx.math.MathUtils.random;

public class SpawnSystem extends IteratingSystem {
    final ECSEngine engine;
    final HashMap<Vector2, Integer> positions;
    private boolean spaceToSpawn = true;

    private int attempts;

    public SpawnSystem(final ECSEngine engine) {
        super(Family.all(B2DComponent.class, ObjectComponent.class).get());
        this.engine = engine;
        positions = new HashMap<>();
    }

    public void update(float deltaTime) {
        positions.clear();
        super.update(deltaTime);

        float xRand = random(Core.GAMEW/2f - Core.BORDER_SIZE/2f + 4, Core.GAMEW/2f + Core.BORDER_SIZE/2f - 4);
        float yRand = random(Core.GAMEH/2f - Core.BORDER_SIZE/2f + 4, Core.GAMEH/2f + Core.BORDER_SIZE/2f - 4);

        boolean tooClose = false;

        for(Vector2 position : positions.keySet()) {
            if(position.dst(xRand, yRand) < positions.get(position)) {
                tooClose = true;
                break;
            }
        }
        if(!tooClose) {
            engine.createObject(new Vector2(xRand, yRand), ObjectType.MERCURY, false, 10);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);
        ObjectComponent objectComponent = ECSEngine.objCmpMpr.get(entity);
        //if(objectComponent.type != ObjectType.ANOMALY) {
            positions.put(b2dComponent.renderPosition, objectComponent.type.getAnimationType().getDiameter());
        //}
    }
}
