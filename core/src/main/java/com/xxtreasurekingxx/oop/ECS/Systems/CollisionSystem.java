package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ActorComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ParticleComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.ObjectType;
import com.xxtreasurekingxx.oop.World.WorldContactListener;

import static com.xxtreasurekingxx.oop.Core.baseExp;
import static com.xxtreasurekingxx.oop.ECS.Components.ParticleComponent.ParticlesType.EXPLOSION;

public class CollisionSystem extends EntitySystem implements WorldContactListener.CollisionListener {
    private final ECSEngine engine;
    private final Core core;

    public CollisionSystem(final Core core, final ECSEngine engine, final WorldContactListener contactListener) {
        this.engine = engine;
        this.core = core;
        contactListener.addCollisionListener(this);
    }

    @Override
    public void startCollision(Entity entityA, Entity entityB) {
        final B2DComponent BCA = ECSEngine.b2dCmpMpr.get(entityA);
        final B2DComponent BCB = ECSEngine.b2dCmpMpr.get(entityB);

        final ObjectComponent OCA = ECSEngine.objCmpMpr.get(entityA);
        final ObjectComponent OCB = ECSEngine.objCmpMpr.get(entityB);

        final ActorComponent ACA = ECSEngine.actCmpMpr.get(entityA);
        final ActorComponent ACB = ECSEngine.actCmpMpr.get(entityB);

        final ParticleComponent PCA = ECSEngine.ptclCmpMpr.get(entityA);
        final ParticleComponent PCB = ECSEngine.ptclCmpMpr.get(entityB);

        if(OCA != null && OCB != null) {
            //delete all objects that collide with black hole (prio 1)
            if(OCA.type == ObjectType.HOLE) {
                BCB.needsDelete = true;
                ACB.needsDelete = true;
                core.getGameData().removeScore(OCB.exp);
            } else if(OCB.type == ObjectType.HOLE) {
                BCA.needsDelete = true;
                ACA.needsDelete = true;
                core.getGameData().removeScore(OCA.exp);
            }
            //do nothing more if any objects should not do anything on collide (prio 2)
            if(OCA.type == ObjectType.ANOMALY || OCB.type == ObjectType.ANOMALY ||
                OCA.type == ObjectType.HOLE    || OCB.type == ObjectType.HOLE ||
                OCA.type == ObjectType.SUN     || OCB.type == ObjectType.SUN) {
                return;
            }

            if(OCA.type != OCB.type) {
                return;
            }

            if(OCA.exp == OCB.exp) {
                BCB.needsDelete = true;
                ACB.needsDelete = true;
                OCA.exp += OCA.exp == 0 ? baseExp : OCB.exp;
                core.getGameData().addScore(OCA.exp == 0 ? baseExp : OCB.exp);
                core.getGameData().addTokens(OCA.type.getUpgradeThreshold());
            } else if(OCA.exp > OCB.exp) {
                BCB.needsDelete = true;
                ACB.needsDelete = true;
                OCA.exp += OCB.exp > 0 ? OCB.exp : baseExp;
                core.getGameData().addScore(OCB.exp > 0 ? OCB.exp : baseExp);
                core.getGameData().addTokens(OCA.type.getUpgradeThreshold());
            } else {
                BCA.needsDelete = true;
                ACA.needsDelete = true;
                OCB.exp += OCA.exp > 0 ? OCA.exp : baseExp;
                core.getGameData().addScore(OCA.exp > 0 ? OCA.exp : baseExp);
                core.getGameData().addTokens(OCB.type.getUpgradeThreshold());
            }
        }
    }

    @Override
    public void endCollision(Entity entityA, Entity entityB) {

    }
}
