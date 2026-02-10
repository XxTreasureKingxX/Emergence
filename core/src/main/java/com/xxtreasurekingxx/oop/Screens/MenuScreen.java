package com.xxtreasurekingxx.oop.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.xxtreasurekingxx.oop.Core;

public class MenuScreen implements Screen {
    private final Core core;
    private SpriteBatch batch;
    private MenuScene scene;

    public MenuScreen(final Core core) {
        this.core = core;
    }

    @Override
    public void show() {
        batch = core.getBatch();
        scene = new MenuScene(core);
        Gdx.input.setInputProcessor(new InputMultiplexer(core.getInputManager(), scene.getStage()));
    }

    @Override
    public void update(float delta) {
        scene.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 1, 0, 0);

        batch.setProjectionMatrix(scene.getStage().getCamera().combined);
        scene.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        System.out.println("Menu Screen Disposed");
        scene.getStage().dispose();
    }
}
