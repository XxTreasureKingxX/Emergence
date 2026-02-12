package com.xxtreasurekingxx.oop.Screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.xxtreasurekingxx.oop.Core;

public class LoadingScreen implements Screen {
    private final Core core;
    private AssetManager assetManager;

    public LoadingScreen(final Core core) {
        this.core = core;
    }

    @Override
    public void show() {
        System.out.println("Switched To Loading Screen");
        assetManager = core.getAssetManager();
        queueAssets();
    }

    @Override
    public void update(float delta) {
        if(assetManager.update()) {
            core.setScreen(core.menuScreen);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 1, 0);
        update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void queueAssets() {
        assetManager.load("ui/menuBackground.png", Texture.class);
        assetManager.load("ui/gameBackground.png", Texture.class);
        assetManager.load("noSpawnArea.png", Texture.class);
        assetManager.load("noSpawnAreaBorder.png", Texture.class);
        assetManager.load("ui/main.atlas", TextureAtlas.class);
        assetManager.load("objects.atlas", TextureAtlas.class);
        assetManager.load("ring.atlas", TextureAtlas.class);
        assetManager.load("particles/circle.p", ParticleEffect.class);
    }
}
