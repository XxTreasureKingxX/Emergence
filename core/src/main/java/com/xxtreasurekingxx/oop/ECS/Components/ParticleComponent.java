package com.xxtreasurekingxx.oop.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ParticleComponent implements Pool.Poolable, Component {
    public Array<ParticleEffectPool.PooledEffect> particles;
    public ParticlesType type;
    public Vector2 position;
    public float scale;

    @Override
    public void reset() {
        for(ParticleEffectPool.PooledEffect particle : particles) {
            if(particle != null) {
                particle.free();
                particle = null;
            }
            type = null;
            position = new Vector2(0, 0);
            scale = 0;
        }
    }

    public enum ParticlesType {
        EXPLOSION("particles/circle.p");

        private final String path;

        ParticlesType(final String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
