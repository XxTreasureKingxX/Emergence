package com.xxtreasurekingxx.oop.Screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;

import static com.xxtreasurekingxx.oop.Core.GAMEH;
import static com.xxtreasurekingxx.oop.Core.GAMEW;

public class MenuScene {
    private final Core core;
    private SpriteBatch batch;
    private Stage stage;
    private FitViewport viewport;
    private AssetManager assetManager;
    private BitmapFont font;
    private TextureAtlas atlas;

    private Table background;

    private TextButton.TextButtonStyle buttonStyle;

    private TextButton playButton;
    private TextButton exitButton;
    private TextButton settingsButton;

    public MenuScene(final Core core) {
        this.core = core;
        batch = core.getBatch();
        assetManager = core.getAssetManager();
        viewport = new FitViewport(GAMEW, GAMEH, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        initFiles();
        createStyles();
        createTables();
        createButtons();

        background.add(playButton);
        stage.addActor(background);
    }

    public void update(float delta) {
        viewport.apply();
        stage.act(delta);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void createStyles() {
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(atlas.findRegion("buttonUp.png"));
        buttonStyle.down = new TextureRegionDrawable(atlas.findRegion("buttonOver.png"));
        buttonStyle.over = new TextureRegionDrawable(atlas.findRegion("buttonOver.png"));
    }

    private void createTables() {
        background = new Table();
        background.setFillParent(true);
        background.background(new TextureRegionDrawable(assetManager.get("ui/menuBackground.png", Texture.class)));
    }

    private void createButtons() {
        playButton = new TextButton("PLAY", buttonStyle);
        playButton.setPosition(0 + GAMEW/25f, GAMEH / 2f - playButton.getHeight() / 2);
        playButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("clicked play button");
                    core.setScreen(core.gameScreen);
                }
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    System.out.println("entered play button");
                    //playButton.setColor(Color.GREEN);
                }
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    playButton.setColor(Color.WHITE);
                }
            }
        );
    }

    private void initFiles() {
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        atlas = assetManager.get("ui/main.atlas", TextureAtlas.class);
    }

    public Stage getStage() {
        return stage;
    }
}
