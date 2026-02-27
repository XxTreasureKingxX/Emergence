package com.xxtreasurekingxx.oop.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.ECS.Systems.BlackHoleSystem;
import com.xxtreasurekingxx.oop.ECS.Systems.GameUISystem;
import com.xxtreasurekingxx.oop.ECS.Systems.SpawnSystem;
import com.xxtreasurekingxx.oop.Input.GameKeys;
import com.xxtreasurekingxx.oop.Input.InputListener;
import com.xxtreasurekingxx.oop.Input.InputManager;
import com.xxtreasurekingxx.oop.World.ObjectType;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.xxtreasurekingxx.oop.Core.*;

public class GameScreen implements Screen, InputListener {
    private final Core core;
    private ECSEngine engine;
    private SpriteBatch batch;
    private final FitViewport viewport;

    public GameScreen(final Core core) {
        this.core = core;
        core.getInputManager().addListener(this);
        batch = core.getBatch();
        viewport = new FitViewport(Core.GAMEW, Core.GAMEH, new OrthographicCamera());
    }

    @Override
    public void show() {
        System.out.println( "Switched To Game Screen");
        //Gdx.input.setInputProcessor(core.getInputManager());
        engine = new ECSEngine(core, viewport);
        GameStart(1);
    }

    @Override
    public void update(float delta) {
        engine.getSystem(GameUISystem.class).update(delta);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0.33f, 0.66f, 0);
        viewport.apply();
        update(delta);

        engine.update(delta);

        batch.setProjectionMatrix(engine.getSystem(GameUISystem.class).getStage().getCamera().combined);
        engine.getSystem(GameUISystem.class).getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        if(engine != null) {
            engine.resize(width, height);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        engine.hide();
    }

    @Override
    public void dispose() {
        System.out.println("Game Screen Disposed");
        engine.getSystem(GameUISystem.class).getStage().dispose();
    }

    private void GameStart(final int spawnCount) {
        engine.createGameBorder();
        core.getGameData().addScore((spawnCount - 1) * baseExp);
        Array<Vector2> positions = genPositions(spawnCount);

        for(int i = 0; i < spawnCount - 1; i++) {
            engine.createObject(positions.first(), ObjectType.MERCURY, false, baseExp, false);
            positions.removeValue(positions.first(), true);
        }
        engine.createObject(positions.first(), ObjectType.ANOMALY, false, 0, false);
    }

    private Array<Vector2> genPositions(final int spawnCount) {
        Array<Vector2> positions = new Array<>();
        int attempts = 1000;

        for(int i = 0; i < spawnCount; i++) {
            float xRand = random(Core.GAMEW/2f - Core.BORDER_SIZE/2f + antiSpawnRadiusDistance/2f, Core.GAMEW/2f + Core.BORDER_SIZE/2f - antiSpawnRadiusDistance/2f);
            float yRand = random(Core.GAMEH/2f - Core.BORDER_SIZE/2f + antiSpawnRadiusDistance/2f, Core.GAMEH/2f + Core.BORDER_SIZE/2f - antiSpawnRadiusDistance/2f);

            boolean tooClose = false;

            for(Vector2 position : positions) {
                if(position.dst(xRand, yRand) < antiSpawnRadiusDistance) {
                    tooClose = true;
                    break;
                }
            }

            if(tooClose && attempts >= 0) {
                i--;
                attempts--;
            } else {
                positions.add(new Vector2(xRand, yRand));
                attempts = 1000;
            }
        }
        return positions;
    }

    private void attemptBlackHole(final float x, final float y) {
        if(engine != null && engine.blackHoleCounter < 1) {
            Vector2 spawnPoint = viewport.unproject(new Vector2(x, y));
            engine.blackHoleCounter++;
            engine.getSystem(BlackHoleSystem.class).spawnBlackHole(new Vector2(spawnPoint.x, spawnPoint.y), ObjectType.HOLE);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(engine == null) {
            return false;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        attemptBlackHole(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
