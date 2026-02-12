package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.AnimationComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ParticleComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.World.AnimationType;

import java.util.EnumMap;

import static com.xxtreasurekingxx.oop.Core.REGION_SIZE;

public class RenderSystem extends EntitySystem {
    private final Core core;
    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final FitViewport viewport;
    private final World world;

    private final Box2DDebugRenderer debugRenderer;

    private ImmutableArray<Entity> ringEntities;
    private ImmutableArray<Entity> particleEntities;
    private ImmutableArray<Entity> animatedEntities;
    private EnumMap<AnimationType, Animation<Sprite>> animations;

    public RenderSystem(final Core core, final SpriteBatch batch, final World world, final FitViewport viewport) {
        this.core = core;
        this.batch = batch;
        this.world = world;
        this.viewport = viewport;

        assetManager = core.getAssetManager();

        debugRenderer = new Box2DDebugRenderer();
        animations = new EnumMap<>(AnimationType.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        animatedEntities = getEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class).get());
        particleEntities = getEngine().getEntitiesFor(Family.all(ParticleComponent.class).get());
    }

    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        ringEntities = getEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class).exclude(ObjectComponent.class).get());

        for(Entity ring : ringEntities) {
            final AnimationComponent animationComponent = ECSEngine.aniCmpMpr.get(ring);
            final B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(ring);

            if(animationComponent.type != null) {
                final Animation<Sprite> animation = getAnimation(animationComponent.type);
                final Sprite frame = animation.getKeyFrame(animationComponent.animationTimer);
                b2dComponent.renderPosition.set(b2dComponent.renderPosition.x, b2dComponent.renderPosition.y);

                float scale = Interpolation.pow2Out.apply((animationComponent.animationTimer+2.2f));

                float width = b2dComponent.width;
                float height = b2dComponent.height;
                width *= scale;
                height *= scale;

                if(frame.getColor().a > 0 && animationComponent.animationTimer <= 1.5f) {
                    frame.setAlpha(1-(animationComponent.animationTimer));
                } else {
                    frame.setAlpha(0);
                }

                frame.setBounds(b2dComponent.renderPosition.x - width/2, b2dComponent.renderPosition.y - height/2, width, height);
                frame.setOriginCenter();
                frame.draw(batch);
            }
        }

        for(Entity entity : animatedEntities) {
            final AnimationComponent animationComponent = ECSEngine.aniCmpMpr.get(entity);
            final B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);

            if(animationComponent.type != null && animationComponent.type != AnimationType.RING) {
                final Animation<Sprite> animation = getAnimation(animationComponent.type);
                animation.setPlayMode(Animation.PlayMode.LOOP);
                final Sprite frame = animation.getKeyFrame(animationComponent.animationTimer);
                b2dComponent.renderPosition.set(b2dComponent.body.getPosition().x, b2dComponent.body.getPosition().y);

                float width = b2dComponent.width;
                float height = b2dComponent.height;

                if(animationComponent.type == AnimationType.HOLE) {
                    width *= 2.3f;
                    height *= 2.3f;
                } else if(animationComponent.type == AnimationType.URANUS || animationComponent.type == AnimationType.SATURN) {
                    width *= 1.195f;
                    height *= 1.195f;
                }

                frame.setBounds(b2dComponent.renderPosition.x - width/2, b2dComponent.renderPosition.y - height/2, width, height);
                frame.setOriginCenter();
                frame.setRotation((float)Math.toDegrees(b2dComponent.angle));
                frame.draw(batch);
            }
        }
        for(Entity entity : particleEntities) {
            ParticleComponent particleComponent = ECSEngine.ptclCmpMpr.get(entity);
            for(ParticleEffect particle : particleComponent.particles) {
                if(particle != null) {
                    particle.draw(batch);
                }
            }
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.end();
        //debugRenderer.render(world, viewport.getCamera().combined);
    }

    public Animation<Sprite> getAnimation(AnimationType type) {
        Animation<Sprite> animation = animations.get(type);
        if(animation == null) {
            System.out.println("Creating animation for " + type);
            final TextureAtlas.AtlasRegion region = assetManager.get(type.getAtlas(), TextureAtlas.class).findRegion(type.getRegion());
            final TextureRegion[][] textureRegions = region.split(REGION_SIZE, REGION_SIZE);
            animation = new Animation<>(type.getFrameRate(), getAnimationSequence(textureRegions[type.getRow()]));
            animations.put(type, animation);
        }
        return animation;
    }

    public Sprite[] getAnimationSequence(final TextureRegion[] regions) {
        final Sprite[] sequence = new Sprite[regions.length];
        int i = 0;
        for(final TextureRegion region : regions) {
            final Sprite sprite = new Sprite(region);
            sequence[i++] = sprite;
        }
        return sequence;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        animatedEntities = null;
        animations.clear();
        debugRenderer.dispose();
    }
}
