package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.ObjectType;

import java.util.HashMap;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.xxtreasurekingxx.oop.Core.antiSpawnRadiusDistance;
import static com.xxtreasurekingxx.oop.Core.baseExp;

public class SpawnSystem extends IteratingSystem {
    private final Core core;
    private final ECSEngine engine;
    private final HashMap<Vector2, Integer> positions;
    private ImmutableArray<Entity> entities;

    public SpawnSystem(final Core core, final ECSEngine engine) {
        super(Family.all(B2DComponent.class, ObjectComponent.class).get());
        this.core = core;
        this.engine = engine;
        positions = new HashMap<>();
    }

    public void update(float deltaTime) {
        super.update(deltaTime);
        entities = getEntities();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    public void spawnAnomaly() {
        int attempts = 15000;
        for(Entity entity : entities) {
            B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);
            ObjectComponent objectComponent = ECSEngine.objCmpMpr.get(entity);
            positions.put(b2dComponent.renderPosition, (objectComponent.type.getAnimationType().getDiameter() + antiSpawnRadiusDistance)/2);
        }

        boolean tooClose = true;
        while(tooClose && attempts > 0) {
            float xRand = random(Core.GAMEW/2f - Core.BORDER_SIZE/2f + antiSpawnRadiusDistance/2f, Core.GAMEW/2f + Core.BORDER_SIZE/2f - antiSpawnRadiusDistance/2f);
            float yRand = random(Core.GAMEH/2f - Core.BORDER_SIZE/2f + antiSpawnRadiusDistance/2f, Core.GAMEH/2f + Core.BORDER_SIZE/2f - antiSpawnRadiusDistance/2f);

            tooClose = false;
            for(Vector2 position : positions.keySet()) {
                if(position.dst(xRand, yRand) < positions.get(position)) {
                    tooClose = true;
                    attempts--;
                    if(attempts == 0) {
                        core.getGameData().addTokens(1);
                    }
                    break;
                }
            }
            if(!tooClose) {
                engine.createObject(new Vector2(xRand, yRand), ObjectType.ANOMALY, false, 0);
            }
        }
        positions.clear();
    }

    public void spawnBaseObject() {
        int attempts = 15000;
        for(Entity entity : entities) {
            B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);
            ObjectComponent objectComponent = ECSEngine.objCmpMpr.get(entity);
            positions.put(b2dComponent.renderPosition, (objectComponent.type.getAnimationType().getDiameter() + antiSpawnRadiusDistance)/2);
        }

        boolean tooClose = true;
        while(tooClose && attempts > 0) {
            float xRand = random(Core.GAMEW/2f - Core.BORDER_SIZE/2f + antiSpawnRadiusDistance/2f, Core.GAMEW/2f + Core.BORDER_SIZE/2f - antiSpawnRadiusDistance/2f);
            float yRand = random(Core.GAMEH/2f - Core.BORDER_SIZE/2f + antiSpawnRadiusDistance/2f, Core.GAMEH/2f + Core.BORDER_SIZE/2f - antiSpawnRadiusDistance/2f);

            tooClose = false;
            for(Vector2 position : positions.keySet()) {
                if(position.dst(xRand, yRand) < positions.get(position)) {
                    tooClose = true;
                    attempts--;
                    System.out.println(attempts);
                    if(attempts == 0) {
                        core.getGameData().addTokens(1);
                    }
                    break;
                }
            }
            if(!tooClose) {
                core.getGameData().addScore(baseExp);
                engine.createObject(new Vector2(xRand, yRand), ObjectType.MERCURY, false, baseExp);
            }
        }
        positions.clear();
    }
}
