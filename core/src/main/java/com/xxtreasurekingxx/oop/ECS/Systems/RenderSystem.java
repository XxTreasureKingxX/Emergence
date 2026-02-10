package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.AnimationComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
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
    Texture texture;

    private final Box2DDebugRenderer debugRenderer;

    private ImmutableArray<Entity> animatedEntities;
    private EnumMap<AnimationType, Animation<Sprite>> animations;

    public RenderSystem(final Core core, final SpriteBatch batch, final World world, final FitViewport viewport) {
        this.core = core;
        this.batch = batch;
        this.world = world;
        this.viewport = viewport;

        assetManager = core.getAssetManager();
        texture = assetManager.get("ui/gameBackground.png", Texture.class);

        debugRenderer = new Box2DDebugRenderer();
        animations = new EnumMap<>(AnimationType.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        animatedEntities = getEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class).get());
    }

    @Override
    public void update(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(texture, 0, 0, Core.GAMEW, Core.GAMEH);

        for(Entity entity : animatedEntities) {
            final B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);

            float width = b2dComponent.width;
            float height = b2dComponent.height;

            final Sprite noSpawnArea = new Sprite(assetManager.get("noSpawnArea.png", Texture.class));
            //noSpawnArea.setBounds(b2dComponent.renderPosition.x - width * 0.9f, b2dComponent.renderPosition.y - height * 0.9f, width*2 * 0.9f, height*2 * 0.9f);
            noSpawnArea.setBounds(b2dComponent.renderPosition.x - width, b2dComponent.renderPosition.y - height, width*2, height*2);
            noSpawnArea.draw(batch);
        }

        for(Entity entity : animatedEntities) {
            final AnimationComponent animationComponent = ECSEngine.aniCmpMpr.get(entity);
            final B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);

            animationComponent.animationTimer += delta;

            if(animationComponent.type != null) {
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

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
