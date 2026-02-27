package com.xxtreasurekingxx.oop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
    public static final int REGION_SIZE = 512;
    public static final int baseExp = 1;
    public static final int antiSpawnRadiusDistance = 32;

    public static final short BIT_OBJECT = 1 << 0;
    public static final short BIT_WALL = 1 << 1;

    public static boolean tutorial = false;

    private SplashScreen splashScreen;
    private LoadingScreen loadingScreen;
    public MenuScreen menuScreen;
    public GameScreen gameScreen;

    private GameData gameData;
    private SpriteBatch batch;
    private AssetManager assetManager;
    private InputManager inputManager;

    private InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        inputManager = new InputManager();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputManager);
        Gdx.input.setInputProcessor(inputMultiplexer);
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

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
    }
}

/*TODO:
   Implement the tick system so the timer/timers can be paused when menus are opened
   Fix hand area bounds
   Work on art <33

   Rumble sfx when bigger planets combine

   //Spawning a black hole will reduce score to reward skill and strategic plays & combos
*/
