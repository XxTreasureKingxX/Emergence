package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;

public class BackgroundRenderSystem extends EntitySystem {
    private final Core core;
    private final SpriteBatch batch;
    private final FitViewport viewport;
    private AssetManager assetManager;
    private Texture texture;

    public BackgroundRenderSystem(final Core core, final SpriteBatch batch, final FitViewport viewport) {
        this.core = core;
        this.batch = batch;
        this.viewport = viewport;
        assetManager = core.getAssetManager();
        texture = assetManager.get("ui/gameBackground.png", Texture.class);
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(texture, 0, 0, Core.GAMEW, Core.GAMEH);
        batch.end();
    }
}
