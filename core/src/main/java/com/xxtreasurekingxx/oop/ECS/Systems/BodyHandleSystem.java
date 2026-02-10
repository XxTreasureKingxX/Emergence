package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.xxtreasurekingxx.oop.ECS.Components.ActorComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.ObjectType;

import static com.xxtreasurekingxx.oop.Core.BIT_OBJECT;
import static com.xxtreasurekingxx.oop.Core.BIT_WALL;

public class BodyHandleSystem extends IteratingSystem {
    private final World world;
    private final ECSEngine engine;

    public BodyHandleSystem(final ECSEngine engine, final World world) {
        super(Family.all(B2DComponent.class).get());
        this.world = world;
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DComponent b2DComponent = ECSEngine.b2dCmpMpr.get(entity);
        ObjectComponent objectComponent = ECSEngine.objCmpMpr.get(entity);

        if(b2DComponent.needsDelete) {
            destroyBody(b2DComponent, entity);
            return;
        }

        if(b2DComponent.needsBody) {
            createBody(entity, b2DComponent, objectComponent);
        }

        if(b2DComponent.needsSquareBody) {
            createSquareBody(entity, b2DComponent);
        }

        if(b2DComponent.isBorder) {
            createBorder(entity, b2DComponent);
        }

        if(objectComponent != null  && engine.blackHolePosition != null && objectComponent.type == ObjectType.ANOMALY) {
            applyAttraction(b2DComponent, engine.blackHolePosition, objectComponent.type.getDensity());
        } else if(objectComponent != null  && engine.blackHolePosition == null && objectComponent.type == ObjectType.ANOMALY) {
           // b2DComponent.body.setLinearVelocity(new Vector2(0, 0));
            b2DComponent.body.setAngularVelocity(0);
        }
    }

    private void createBody(final Entity entity, final B2DComponent b2DComponent, final ObjectComponent objectComponent) {
        b2DComponent.needsBody = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(b2DComponent.renderPosition.x, b2DComponent.renderPosition.y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.angle = b2DComponent.angle;

        b2DComponent.body = world.createBody(bodyDef);
        b2DComponent.body.setUserData(entity);
        b2DComponent.body.setLinearDamping(1);
        b2DComponent.body.setAngularDamping(1);

        CircleShape shape = new CircleShape();
        shape.setRadius(b2DComponent.width / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = BIT_OBJECT;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.isSensor = !b2DComponent.shouldCollide;
        fixtureDef.density = objectComponent.type.getDensity();

        b2DComponent.body.createFixture(fixtureDef);
        shape.dispose();
    }

    private void createSquareBody(final Entity entity, final B2DComponent b2DComponent) {
        System.out.println("Creating Square Body");
        b2DComponent.needsSquareBody = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(b2DComponent.renderPosition.x, b2DComponent.renderPosition.y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.angle = b2DComponent.angle;

        b2DComponent.body = world.createBody(bodyDef);
        b2DComponent.body.setUserData(entity);

        PolygonShape box = new PolygonShape();
        box.setAsBox(b2DComponent.width / 2, b2DComponent.height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.filter.categoryBits = BIT_OBJECT;
        fixtureDef.filter.maskBits = -1;

        b2DComponent.body.createFixture(fixtureDef);
        box.dispose();
    }

    private void createBorder(final Entity entity, final B2DComponent b2DComponent) {
        System.out.println("Creating Border");
        b2DComponent.isBorder = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(b2DComponent.renderPosition.x, b2DComponent.renderPosition.y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        b2DComponent.body = world.createBody(bodyDef);
        b2DComponent.body.setUserData(entity);

        final float w = b2DComponent.width;

        ChainShape box = new ChainShape();
        float[] vertices = {0 - w/2, 0 - w/2, w/2, 0 - w/2, w/2, w/2, 0 - w/2, w/2, 0 - w/2, 0 - w/2};
        box.createChain(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.filter.categoryBits = BIT_WALL;
        fixtureDef.filter.maskBits = -1;

        b2DComponent.body.createFixture(fixtureDef);
        box.dispose();
    }

    private void destroyBody(final B2DComponent b2DComponent, final Entity entity) {
        b2DComponent.needsDelete = false;
        b2DComponent.body.getWorld().destroyBody(b2DComponent.body);
        b2DComponent.body = null;
        getEngine().removeEntity(entity);
    }

    private void applyAttraction(final B2DComponent b2DComponent, final Vector2 targetPos, final float speed) {
        final Vector2 direction = new Vector2(targetPos).sub(b2DComponent.body.getPosition());
        direction.nor().scl(speed*10);
        b2DComponent.body.setLinearVelocity(direction);
    }
}
