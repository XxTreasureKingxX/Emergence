package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.xxtreasurekingxx.oop.ECS.Components.ActorComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.ObjectType;

public class EvolutionSystem extends IteratingSystem {
    private final ECSEngine engine;

    public EvolutionSystem(final ECSEngine engine) {
        super(Family.all(ObjectComponent.class, B2DComponent.class, ActorComponent.class).get());
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ObjectComponent objectComponent = ECSEngine.objCmpMpr.get(entity);
        B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);
        ActorComponent actorComponent = ECSEngine.actCmpMpr.get(entity);

        if(objectComponent.exp >= objectComponent.type.getUpgradeThreshold()) {
            actorComponent.needsDelete = true;
            b2dComponent.needsDelete = true;
            if(objectComponent.type != ObjectType.SUN) {
                upgradeObject(objectComponent.type.getUpgrade(), b2dComponent, objectComponent);
            }
        }
    }

    private void upgradeObject(final ObjectType type, final B2DComponent b2dComponent, final ObjectComponent objectComponent) {
        engine.createObject(b2dComponent.renderPosition, type, false, objectComponent.exp);
    }
}
