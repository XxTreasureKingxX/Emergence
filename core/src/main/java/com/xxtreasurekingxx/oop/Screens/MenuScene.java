package com.xxtreasurekingxx.oop.Screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;

import static com.xxtreasurekingxx.oop.Core.*;

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
    private TextButton.TextButtonStyle smallButtonStyle;
    private Label.LabelStyle panelStyle;

    private TextButton playButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    private Label infoPanel;

    private TextButton reverseButton;
    private TextButton forwardButton;
    private TextButton gamePlayButton;

    public MenuScene(final Core core) {
        this.core = core;
        batch = core.getBatch();
        assetManager = core.getAssetManager();
        viewport = new FitViewport(GAMEW, GAMEH, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        initFiles();
        createStyles();
        createTables();
        createLabels();
        createButtons();

        background.add(playButton).width(playButton.getWidth()/4).height(playButton.getHeight()/4).row();
        background.add(settingsButton).width(settingsButton.getWidth()/4).height(settingsButton.getHeight()/4);
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

        smallButtonStyle = new TextButton.TextButtonStyle();
        smallButtonStyle.font = font;
        smallButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("smallButtonUp.png"));
        smallButtonStyle.down = new TextureRegionDrawable(atlas.findRegion("smallButtonOver.png"));
        smallButtonStyle.over = new TextureRegionDrawable(atlas.findRegion("smallButtonOver.png"));

        panelStyle = new Label.LabelStyle();
        panelStyle.font = font;
        panelStyle.background = new TextureRegionDrawable(atlas.findRegion("pane.png"));
    }

    private void createTables() {
        background = new Table();
        background.setFillParent(true);
        background.background(new TextureRegionDrawable(assetManager.get("ui/menuBackground.png", Texture.class)));
    }

    private void createLabels() {
        infoPanel = new Label("Tutorial\nTesting", panelStyle);
        infoPanel.setAlignment(Align.center);
        infoPanel.setSize(infoPanel.getWidth()/4, infoPanel.getHeight()/4);
        infoPanel.setPosition(GAMEW/2f - infoPanel.getWidth()/2f, GAMEH/2f - infoPanel.getHeight()/2f);
    }

    private void createButtons() {
        playButton = new TextButton("PLAY", buttonStyle);
        playButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if(!tutorial) {
                        tutorial = true;
                        openTutorial();
                    }
                }
            }
        );

        gamePlayButton = new TextButton("START", buttonStyle);
        gamePlayButton.setSize(gamePlayButton.getWidth()/4, gamePlayButton.getHeight()/4);
        gamePlayButton.setPosition(GAMEW/2f - gamePlayButton.getWidth()/2f, GAMEH/2f - gamePlayButton.getHeight()/2f - infoPanel.getHeight()/2f);
        gamePlayButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    core.setScreen(core.gameScreen);
                }
            }
        );

        settingsButton = new TextButton("SETTINGS", buttonStyle);
        settingsButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {

                }
            }
        );

        reverseButton = new TextButton("<", smallButtonStyle);
        reverseButton.setSize(reverseButton.getWidth()/4, reverseButton.getHeight()/4);
        reverseButton.setPosition(GAMEW/2f - reverseButton.getWidth()/2f - infoPanel.getWidth()/2, GAMEH/2f - reverseButton.getHeight()/2f);

        forwardButton = new TextButton(">", smallButtonStyle);
        forwardButton.setSize(forwardButton.getWidth()/4, forwardButton.getHeight()/4);
        forwardButton.setPosition(GAMEW/2f - forwardButton.getWidth()/2f + infoPanel.getWidth()/2, GAMEH/2f - forwardButton.getHeight()/2f);
    }

    private void initFiles() {
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        atlas = assetManager.get("ui/main.atlas", TextureAtlas.class);
    }

    private void openTutorial() {
        stage.addActor(infoPanel);
        stage.addActor(reverseButton);
        stage.addActor(forwardButton);
        stage.addActor(gamePlayButton);
    }

    public Stage getStage() {
        return stage;
    }
}
