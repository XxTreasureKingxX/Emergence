package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.ActorComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;

public class GameUISystem extends IteratingSystem {
    final Core core;
    private SpriteBatch batch;
    private Stage stage;
    private final FitViewport viewport;
    private AssetManager assetManager;
    private BitmapFont font;
    private TextureAtlas atlas;

    private Table uiTable;

    private Label.LabelStyle blankLabelStyle;
    private Label.LabelStyle blackLabelStyle;
    private Label scoreLabel;

    public GameUISystem(final Core core, final FitViewport viewport) {
        super(Family.all(ActorComponent.class, B2DComponent.class, ObjectComponent.class).get());
        this.core = core;
        this.viewport = viewport;
        batch = core.getBatch();
        assetManager = core.getAssetManager();
        stage = new Stage(viewport, batch);

        initFiles();
        initStyles();

        scoreLabel = new Label("Score:" + core.getGameData().getScore(), blackLabelStyle);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setFontScale(1f);
        scoreLabel.setPosition(Core.GAMEW/2f - scoreLabel.getWidth()/2, Core.GAMEH - scoreLabel.getHeight() * 1.5f);

        stage.addActor(scoreLabel);
    }

    public void update(float delta) {
        super.update(delta);

        System.out.println(core.getGameData().getScore());
        scoreLabel.setText("Score:" + core.getGameData().getScore());

        viewport.apply();
        stage.act(delta);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void initStyles() {
        blankLabelStyle = new Label.LabelStyle();
        blankLabelStyle.font = font;
        blankLabelStyle.fontColor = Color.BLACK;
        blankLabelStyle.background = new TextureRegionDrawable(atlas.findRegion("blank.png"));

        blackLabelStyle = new Label.LabelStyle();
        blackLabelStyle.font = font;
        blackLabelStyle.fontColor = Color.WHITE;
        blackLabelStyle.background = new TextureRegionDrawable(atlas.findRegion("label.png"));
    }

    private void initFiles() {
        font = new BitmapFont();
        font.getData().setScale(0.5f);

        atlas = assetManager.get("ui/main.atlas", TextureAtlas.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ActorComponent actorComponent = ECSEngine.actCmpMpr.get(entity);
        ObjectComponent objectComponent = ECSEngine.objCmpMpr.get(entity);
        B2DComponent b2dComponent = ECSEngine.b2dCmpMpr.get(entity);

        if(actorComponent.needsDelete && actorComponent.actor instanceof Label) {
            actorComponent.needsDelete = false;
            actorComponent.actor.remove();
            actorComponent.actor = null;
            return;
        }

        if(actorComponent.needsActor) {
            actorComponent.needsActor = false;
            actorComponent.actor = new Label(objectComponent.exp + "/" + objectComponent.type.getUpgradeThreshold(), blankLabelStyle);
            actorComponent.actor.setPosition(b2dComponent.renderPosition.x - b2dComponent.width/2, b2dComponent.renderPosition.y - b2dComponent.height/2);
            actorComponent.actor.setSize(b2dComponent.width, b2dComponent.height);
            ((Label)actorComponent.actor).setAlignment(Align.center);
            stage.addActor(actorComponent.actor);
        }

        if(actorComponent.actor instanceof Label) {
            ((Label)actorComponent.actor).setText(objectComponent.exp + "/" + objectComponent.type.getUpgradeThreshold());
            actorComponent.actor.setPosition(b2dComponent.renderPosition.x - b2dComponent.width/2, b2dComponent.renderPosition.y - b2dComponent.height/2);
        }
    }

    public Stage getStage() {
        return stage;
    }
}
