package com.xxtreasurekingxx.oop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xxtreasurekingxx.oop.Input.InputManager;
import com.xxtreasurekingxx.oop.Screens.GameScreen;
import com.xxtreasurekingxx.oop.Screens.LoadingScreen;
import com.xxtreasurekingxx.oop.Screens.MenuScreen;
import com.xxtreasurekingxx.oop.Screens.SplashScreen;
import com.xxtreasurekingxx.oop.World.WorldContactListener;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    public static final int GAMEH = 270;
    public static final int GAMEW = 480;
    public static final float BORDER_SIZE = GAMEH * 0.71f;
    public static final float FPS = 1 / 60f;
    public static final int REGION_SIZE = 128;

    public static final short BIT_OBJECT = 1 << 0;
    public static final short BIT_WALL = 1 << 1;

    private SplashScreen splashScreen;
    private LoadingScreen loadingScreen;
    public MenuScreen menuScreen;
    public GameScreen gameScreen;

    private GameData gameData;
    private SpriteBatch batch;
    private AssetManager assetManager;
    private InputManager inputManager;

    @Override
    public void create() {
        inputManager = new InputManager();
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        gameData = new GameData(this);

        splashScreen = new SplashScreen();
        loadingScreen = new LoadingScreen(this);
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);

        setScreen(loadingScreen);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public GameData getGameData() {
        return gameData;
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
    }
}
