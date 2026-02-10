package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.xxtreasurekingxx.oop.ECS.Components.AnimationComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.BlackHoleComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.ObjectType;

import static com.badlogic.gdx.math.MathUtils.random;

public class BlackHoleSystem extends IteratingSystem {
    private final ECSEngine engine;

    public BlackHoleSystem(final ECSEngine engine) {
        super(Family.all(BlackHoleComponent.class, B2DComponent.class).get());
        this.engine = engine;
    }

    public void spawnBlackHole(final Vector2 pos, final ObjectType type) {
        System.out.println("Creating Black Hole");
        Entity object = getEngine().createEntity();

        //B2DComponent
        B2DComponent b2DComponent = getEngine().createComponent(B2DComponent.class);
        b2DComponent.renderPosition = new Vector2(pos);
        b2DComponent.width = type.getAnimationType().getDiameter();
        b2DComponent.height = type.getAnimationType().getDiameter();
        b2DComponent.needsBody = true;
        b2DComponent.shouldCollide = false;
        b2DComponent.angle = random(0, 360);
        object.add(b2DComponent);

        //AnimationComponent
        AnimationComponent animationComponent = getEngine().createComponent(AnimationComponent.class);
        animationComponent.type = type.getAnimationType();
        object.add(animationComponent);

        //ObjectComponent
        ObjectComponent objectComponent = getEngine().createComponent(ObjectComponent.class);
        objectComponent.type = type;
        object.add(objectComponent);

        //BlackHoleComponent
        BlackHoleComponent blackHoleComponent = getEngine().createComponent(BlackHoleComponent.class);
        blackHoleComponent.coolDown = 2;
        blackHoleComponent.coolDownTimer = 2;
        object.add(blackHoleComponent);

        getEngine().addEntity(object);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DComponent b2DComponent = ECSEngine.b2dCmpMpr.get(entity);
        BlackHoleComponent blackHoleComponent = ECSEngine.blkHoleCmpMpr.get(entity);

        engine.blackHolePosition = b2DComponent.renderPosition;
        blackHoleComponent.coolDownTimer -= deltaTime;

        if(blackHoleComponent.coolDownTimer <= 0) {
            engine.blackHoleCounter--;
            b2DComponent.needsDelete = true;
            engine.blackHolePosition = null;
        }
    }
}
