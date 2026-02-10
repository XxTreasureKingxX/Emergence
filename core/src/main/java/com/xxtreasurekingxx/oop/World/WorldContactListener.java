package com.xxtreasurekingxx.oop.World;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.oop.ECS.Components.CollisionComponent;

public class WorldContactListener implements ContactListener {
    public static Array<CollisionListener> listeners;

    public WorldContactListener() {
        listeners = new Array<>();
    }

    @Override
    public void beginContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        final Entity entityA = (Entity) fixtureA.getBody().getUserData();
        final Entity entityB = (Entity) fixtureB.getBody().getUserData();

        if(entityA != null && entityB != null) {
            if(isCollision(fixtureA, entityA, fixtureB, entityB)) {
                CollisionComponent component = new CollisionComponent();
                entityA.add(component);
                entityB.add(component);

                for(CollisionListener listener : listeners) {
                    listener.startCollision(entityA, entityB);
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        final Entity entityA = (Entity) fixtureA.getBody().getUserData();
        final Entity entityB = (Entity) fixtureB.getBody().getUserData();

        if(entityA != null && entityB != null) {
            if(isCollision(fixtureA, entityA, fixtureB, entityB)) {
                entityA.remove(CollisionComponent.class);
                entityB.remove(CollisionComponent.class);

                for(CollisionListener listener : listeners) {
                    listener.endCollision(entityA, entityB);
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean isCollision(Fixture F1, Entity E1, Fixture F2, Entity E2) {
        return (F1.getBody().getUserData() == E1 && F2.getBody().getUserData() == E2) || (F1.getBody().getUserData() == E2 && F2.getBody().getUserData() == E1);
    }

    public void addCollisionListener(CollisionListener listener) {
        listeners.add(listener);
    }

    public void removeCollisionListener(CollisionListener listener) {
        listeners.removeValue(listener, true);
    }

    public interface CollisionListener {
        void startCollision(final Entity entityA, final Entity entityB);
        void endCollision(final Entity entityA, final Entity entityB);
    }
}
