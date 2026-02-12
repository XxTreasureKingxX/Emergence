package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.xxtreasurekingxx.oop.ECS.Components.AnimationComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.AnimationType;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final AnimationComponent animationComponent = ECSEngine.aniCmpMpr.get(entity);
        animationComponent.animationTimer += deltaTime;

        if(animationComponent.type == AnimationType.RING) {
            if(animationComponent.animationTimer >= 1.5f) {
                getEngine().removeEntity(entity);
            }
        }
    }
}
