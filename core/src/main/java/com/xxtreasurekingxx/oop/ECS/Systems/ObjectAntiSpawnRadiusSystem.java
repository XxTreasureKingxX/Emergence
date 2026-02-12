package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.AnimationComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.BlackHoleComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.xxtreasurekingxx.oop.Core.antiSpawnRadiusDistance;

public class ObjectAntiSpawnRadiusSystem extends EntitySystem {
    private final Core core;
    private final SpriteBatch batch;
    private final FitViewport viewport;
    private AssetManager assetManager;
    private FrameBuffer fbo;
    private TextureRegion fboRegion;
    private ImmutableArray<Entity> animatedEntities;
    private Sprite noSpawnArea;
    private Sprite noSpawnAreaBorder;
    private float time;

    private ShaderProgram shader;

    public ObjectAntiSpawnRadiusSystem(final Core core, final SpriteBatch batch, final FitViewport viewport) {
        this.core = core;
        this.batch = batch;
        this.viewport = viewport;
        assetManager = core.getAssetManager();
        noSpawnArea = new Sprite(assetManager.get("noSpawnArea.png", Texture.class));
        noSpawnAreaBorder = new Sprite(assetManager.get("noSpawnAreaBorder.png", Texture.class));

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Core.GAMEW, Core.GAMEH, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboRegion.flip(false, true);

        shader = new ShaderProgram(Gdx.files.internal("shaders/vertex.glsl").readString(), Gdx.files.internal("shaders/fragment.glsl").readString());
        if(!shader.isCompiled()) {
            System.out.println(shader.getLog());
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        animatedEntities = getEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class, ObjectComponent.class).exclude(BlackHoleComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        time += deltaTime;

        fbo.begin();
        ScreenUtils.clear(0, 0, 0, 0);
        Gdx.gl.glViewport(0, 0, fbo.getWidth(), fbo.getHeight());

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for(Entity entity : animatedEntities) {
            final B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);

            float width = b2dComponent.width;
            float height = b2dComponent.height;

            noSpawnArea.setBounds(b2dComponent.renderPosition.x - (width + antiSpawnRadiusDistance)/2, b2dComponent.renderPosition.y - (height + antiSpawnRadiusDistance)/2, width + antiSpawnRadiusDistance, height + antiSpawnRadiusDistance);
            noSpawnArea.draw(batch);
        }
        noSpawnAreaBorder.setBounds(Core.GAMEW/2f - Core.BORDER_SIZE/2f * 1.02f, Core.GAMEH/2f - Core.BORDER_SIZE/2f * 1.02f, Core.BORDER_SIZE * 1.02f, Core.BORDER_SIZE * 1.02f);
        noSpawnAreaBorder.draw(batch);

        batch.end();
        fbo.end();

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        viewport.apply();

        float worldX = Core.GAMEW/2f - Core.BORDER_SIZE/2f;
        float worldY = Core.GAMEH/2f - Core.BORDER_SIZE/2f;
        float worldSize = Core.BORDER_SIZE;

        Vector2 screenPos = viewport.project(new Vector2(worldX, worldY));
        Vector2 screenSize = viewport.project(new Vector2(worldX + worldSize, worldY + worldSize));

        Rectangle scissors = new Rectangle(screenPos.x, screenPos.y, screenSize.x - screenPos.x, screenSize.y - screenPos.y);

        if(ScissorStack.pushScissors(scissors)) {
            batch.setShader(shader);
            batch.begin();
            shader.setUniformf("u_time", time);
            //shader.setUniformf("u_resolution", viewport.getScreenWidth(), viewport.getScreenHeight());
            shader.setUniformf("u_aspectRatio", (float) fbo.getWidth() / fbo.getHeight());
            batch.draw(fboRegion, 0, 0, Core.GAMEW, Core.GAMEH);
            batch.end();
            ScissorStack.popScissors();
            batch.setShader(null);
        }
    }

    public void resize(int width, int height) {
        if(fbo != null) {
            fbo.dispose();
        }
        if(width == 0 || height == 0) {
            return;
        }
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboRegion.flip(false, true);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        fbo.dispose();
        shader.dispose();
    }
}
