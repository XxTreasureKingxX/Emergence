package com.xxtreasurekingxx.oop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.xxtreasurekingxx.oop.Screens.GameScreen;
import com.xxtreasurekingxx.oop.Screens.LoadingScreen;
import com.xxtreasurekingxx.oop.Screens.MenuScreen;
import com.xxtreasurekingxx.oop.Screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    public static final int GAMEH = 1080;
    public static final int GAMEW = 1920;
    public static final float FPS = 1 / 60f;

    SplashScreen splashScreen;
    LoadingScreen loadingScreen;
    MenuScreen menuScreen;
    GameScreen gameScreen;

    @Override
    public void create() {
        splashScreen = new SplashScreen();
        loadingScreen = new LoadingScreen();
        menuScreen = new MenuScreen();
        gameScreen = new GameScreen();

        setScreen(splashScreen);
    }
}
