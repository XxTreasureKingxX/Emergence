package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.ParticleComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;

import java.util.EnumMap;

public class ParticleSystem extends IteratingSystem {
    private final Core core;
    private final EnumMap<ParticleComponent.ParticlesType, ParticleEffectPool> particlePools;
    private final AssetManager assetManager;

    public ParticleSystem(final Core core) {
        super(Family.all(ParticleComponent.class).get());
        this.core = core;
        particlePools = new EnumMap<>(ParticleComponent.ParticlesType.class);
        assetManager = core.getAssetManager();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleComponent particleComponent = ECSEngine.ptclCmpMpr.get(entity);

        for(int i = particleComponent.particles.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect particle = particleComponent.particles.get(i);
            if(particle != null) {
                particle.update(deltaTime);
                if(particle.isComplete()) {
                    particleComponent.particles.removeValue(particle, true);
                }
            }
        }
    }

    public void addParticle(ParticleComponent component, float x, float y) {
        ParticleEffectPool pool = getPool(component.type);
        ParticleEffectPool.PooledEffect particle = pool.obtain();
        particle.setPosition(x, y);
        particle.setDuration(1000);
        particle.scaleEffect(component.scale);
        component.particles.add(particle);
    }

    private ParticleEffectPool getPool(ParticleComponent.ParticlesType type) {
        ParticleEffectPool pool = particlePools.get(type);
        if(pool == null) {
            ParticleEffect effect = assetManager.get(type.getPath(), ParticleEffect.class);
            effect.setEmittersCleanUpBlendFunction(false);
            pool = new ParticleEffectPool(effect, 1, 128);
            particlePools.put(type, pool);
        }
        return pool;
    }

    public EnumMap<ParticleComponent.ParticlesType, ParticleEffectPool> getParticlePools() {
        return particlePools;
    }
}
