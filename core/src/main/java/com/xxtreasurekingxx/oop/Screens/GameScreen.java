package com.xxtreasurekingxx.oop.Screens;

import com.badlogic.gdx.Gdx;
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
import com.xxtreasurekingxx.oop.Input.GameKeys;
import com.xxtreasurekingxx.oop.Input.InputListener;
import com.xxtreasurekingxx.oop.Input.InputManager;
import com.xxtreasurekingxx.oop.World.ObjectType;

import static com.badlogic.gdx.math.MathUtils.random;

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
        Gdx.input.setInputProcessor(core.getInputManager());
        engine = new ECSEngine(core, viewport);
        GameStart(4);
    }

    @Override
    public void update(float delta) {
        engine.getSystem(GameUISystem.class).update(delta);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0.33f, 0.66f, 0);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        System.out.println("Game Screen Disposed");
        engine.getSystem(GameUISystem.class).getStage().dispose();
    }

    private void GameStart(final int spawnCount) {
        engine.createGameBorder();
        core.getGameData().addScore(spawnCount * 10);

        Array<Vector2> positions = createPositions(spawnCount);
        boolean anomalySpawned = false;

        for(int i = 0; i < spawnCount; i++) {
            int spawnRand = random(1, 8);

            if(i >= spawnCount-1 && !anomalySpawned) {
                spawnRand = 8;
            }

            if(spawnRand == 8) {
                engine.createObject(positions.get(i), ObjectType.ANOMALY, false, -1);
                anomalySpawned = true;
            } else {
                engine.createObject(positions.get(i), ObjectType.MERCURY, false, 10);
            }
        }
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {

    }

    @Override
    public void keyReleased(InputManager manager, GameKeys key) {

    }

    @Override
    public boolean keyDown(int keycode) {
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
        if(engine != null && engine.blackHoleCounter < 1) {
            Vector2 spawnPoint = viewport.unproject(new Vector2(screenX, screenY));
            spawnBlackHole(spawnPoint.x, spawnPoint.y);
        }
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

    private void spawnBlackHole(final float x, final float y) {
        engine.blackHoleCounter++;
        engine.getSystem(BlackHoleSystem.class).spawnBlackHole(new Vector2(x, y), ObjectType.HOLE);
    }

    private Array<Vector2> createPositions(final int spawnCount) {
        Array<Vector2> positions = new Array<>();
        int attempts = 100;

        for(int i = 0; i < spawnCount; i++) {
            float xRand = random(Core.GAMEW/2f - Core.BORDER_SIZE/2f + 4, Core.GAMEW/2f + Core.BORDER_SIZE/2f - 4);
            float yRand = random(Core.GAMEH/2f - Core.BORDER_SIZE/2f + 4, Core.GAMEH/2f + Core.BORDER_SIZE/2f - 4);

            boolean tooClose = false;

            for(Vector2 position : positions) {
                if(position.dst(xRand, yRand) < 32) {
                    tooClose = true;
                    break;
                }
            }

            if(tooClose && attempts >= 0) {
                i--;
                attempts--;
            } else {
                positions.add(new Vector2(xRand, yRand));
                attempts = 100;
            }
        }
        return positions;
    }
}
