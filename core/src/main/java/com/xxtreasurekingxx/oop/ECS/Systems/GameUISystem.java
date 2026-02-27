package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.CardTypes;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.ActorComponent;
import com.xxtreasurekingxx.oop.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.oop.ECS.Components.CardComponent;
import com.xxtreasurekingxx.oop.ECS.Components.ObjectComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.Input.GameKeys;
import com.xxtreasurekingxx.oop.Input.InputListener;
import com.xxtreasurekingxx.oop.Input.InputManager;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.xxtreasurekingxx.oop.ECS.Systems.SpawnSystem.boardIsFull;
import static com.xxtreasurekingxx.oop.ECS.Systems.SpawnSystem.timerCooldown;

public class GameUISystem extends IteratingSystem implements InputListener {
    final Core core;
    private SpriteBatch batch;
    private Stage stage;
    private final ECSEngine engine;
    private final FitViewport viewport;
    private AssetManager assetManager;
    private BitmapFont font;
    private TextureAtlas atlas;
    private final CardHandleSystem cardSystem;

    private Label.LabelStyle blankLabelStyle;
    private TextButton.TextButtonStyle blankButtonStyle;
    private Label.LabelStyle blackLabelStyle;
    private TextButton.TextButtonStyle tokenStyle;
    private Label scoreLabel;
    private TextButton tokenButton;
    private Table shopPane;
    private boolean shopIsOpen = false;
    private boolean callShopUpdate = false;
    private boolean handIsOpen = false;
    private boolean callHandUpdate = false;
    private boolean handIsHidden = false;
    private final Array<CardTypes.CardType> shopCards;
    private int cardTypeCount;
    private TextButton.TextButtonStyle cardStyle;
    private TextButton.TextButtonStyle ruleCardStyle;
    private TextButton.TextButtonStyle goldCardStyle;
    private TextButton shopCard1;
    private TextButton shopCard2;
    private TextButton shopCard3;

    private TextButton handHiddenButton;
    private TextButton.TextButtonStyle handHiddenStyle;
    private Array<CardTypes.CardType> handCards;
    private TextButton handCard1;
    private TextButton handCard2;
    private TextButton handCard3;
    private TextButton handCard4;
    private TextButton handCard5;
    private TextButton handCard6;

    private ProgressBar cooldownIcon;
    private ProgressBar.ProgressBarStyle cooldownIconStyle;

    public GameUISystem(final Core core, final FitViewport viewport, final ECSEngine engine) {
        super(Family.all(ActorComponent.class, B2DComponent.class, ObjectComponent.class).get());
        this.core = core;
        this.viewport = viewport;
        this.engine = engine;
        cardSystem = engine.getSystem(CardHandleSystem.class);
        batch = core.getBatch();
        assetManager = core.getAssetManager();
        stage = new Stage(viewport, batch);
        core.getInputManager().addListener(this);
        core.getInputMultiplexer().addProcessor(stage);
        core.getInputMultiplexer().removeProcessor(core.getInputManager());
        core.getInputMultiplexer().addProcessor(core.getInputManager());

        initFiles();
        initStyles();

        scoreLabel = new Label("Score: " + core.getGameData().getScore(), blackLabelStyle);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setFontScale(1f);
        scoreLabel.setPosition(Core.GAMEW/2f - scoreLabel.getWidth()/2, Core.GAMEH - scoreLabel.getHeight() * 1.25f);

        tokenButton = new TextButton(core.getGameData().getTokens() + "  ", tokenStyle);
        tokenButton.setSize(48, 48);
        tokenButton.getLabel().setAlignment(Align.center);
        tokenButton.getLabel().setFontScale(0.8f);
        tokenButton.setPosition(0 + tokenButton.getWidth()/8, Core.GAMEH - tokenButton.getHeight() - tokenButton.getHeight()/8);
        tokenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!shopIsOpen) {
                    openShop();
                } else {
                    closeShop();
                }
            }
        });

        shopCards = new Array<>();
        cardTypeCount = 6;
        shopPane = new Table();
        shopPane.setBackground(new TextureRegionDrawable(atlas.findRegion("pane.png")));
        shopPane.setSize(906/4f, 714/4f);
        shopPane.setPosition(Core.GAMEW/2f - shopPane.getWidth()/2, Core.GAMEH/2f - shopPane.getHeight()/2);
        setUpShop();

        stage.addActor(scoreLabel);
        stage.addActor(tokenButton);

        handCards = new Array<>();
        setUpHand();

        cooldownIcon = new ProgressBar(0, 1, 0.125f, true, cooldownIconStyle);
        cooldownIcon.setSize(128/4f, 128/4f);
        cooldownIcon.getStyle().background.setMinWidth(128/4f);
        cooldownIcon.getStyle().knobAfter.setMinWidth(128/4f);
        cooldownIcon.getStyle().disabledKnobAfter.setMinWidth(128/4f);
        cooldownIcon.setPosition(Core.GAMEW/2f - scoreLabel.getWidth(), Core.GAMEH - cooldownIcon.getHeight() - 3);
        stage.addActor(cooldownIcon);
    }

    public void update(float delta) {
        super.update(delta);

        scoreLabel.setText("Score: " + core.getGameData().getScore());
        tokenButton.setText("\n\n\n" + core.getGameData().getTokens() + "  ");
        cooldownIcon.setValue(boardIsFull ? 0 : (getEngine().getSystem(SpawnSystem.class).getTimer()/timerCooldown));
        cooldownIcon.setDisabled(boardIsFull ? true : false);
        updateShop();
        updateHand();

        viewport.apply();
        stage.act(delta);
    }

    private void initStyles() {
        blankLabelStyle = new Label.LabelStyle();
        blankLabelStyle.font = font;
        blankLabelStyle.fontColor = Color.BLACK;
        blankLabelStyle.background = new TextureRegionDrawable(atlas.findRegion("blank.png"));

        blankButtonStyle = new TextButton.TextButtonStyle();
        blankButtonStyle.font = font;
        blankButtonStyle.fontColor = Color.BLACK;
        blankButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("blank.png"));

        blackLabelStyle = new Label.LabelStyle();
        blackLabelStyle.font = font;
        blackLabelStyle.fontColor = Color.WHITE;
        blackLabelStyle.background = new TextureRegionDrawable(atlas.findRegion("label.png"));

        tokenStyle = new TextButton.TextButtonStyle();
        tokenStyle.font = font;
        tokenStyle.fontColor = Color.WHITE;
        tokenStyle.up = new TextureRegionDrawable(atlas.findRegion("token.png"));
        tokenStyle.over = new TextureRegionDrawable(atlas.findRegion("tokenOver.png"));

        cardStyle = new TextButton.TextButtonStyle();
        cardStyle.font = font;
        cardStyle.fontColor = Color.WHITE;
        cardStyle.up = new TextureRegionDrawable(atlas.findRegion("card.png"));
        cardStyle.over = new TextureRegionDrawable(atlas.findRegion("cardOver.png"));

        ruleCardStyle = new TextButton.TextButtonStyle();
        ruleCardStyle.font = font;
        ruleCardStyle.fontColor = Color.WHITE;
        ruleCardStyle.up = new TextureRegionDrawable(atlas.findRegion("ruleCard.png"));
        ruleCardStyle.over = new TextureRegionDrawable(atlas.findRegion("ruleCardOver.png"));

        goldCardStyle = new TextButton.TextButtonStyle();
        goldCardStyle.font = font;
        goldCardStyle.fontColor = Color.WHITE;
        goldCardStyle.up = new TextureRegionDrawable(atlas.findRegion("goldCard.png"));
        goldCardStyle.over = new TextureRegionDrawable(atlas.findRegion("goldCardOver.png"));

        handHiddenStyle = new TextButton.TextButtonStyle();
        handHiddenStyle.font = font;
        handHiddenStyle.fontColor = Color.WHITE;
        handHiddenStyle.up = new TextureRegionDrawable(atlas.findRegion("handHidden.png"));
        handHiddenStyle.checked = new TextureRegionDrawable(atlas.findRegion("handHiddenDown.png"));

        cooldownIconStyle = new ProgressBar.ProgressBarStyle();
        cooldownIconStyle.background = new TextureRegionDrawable(atlas.findRegion("cooldownIconBack.png"));
        cooldownIconStyle.knobAfter = new TextureRegionDrawable(atlas.findRegion("cooldownIconOver.png"));
        cooldownIconStyle.disabledKnobAfter = new TextureRegionDrawable(atlas.findRegion("cooldownIconDisabled.png"));
    }

    private void initFiles() {
        font = new BitmapFont();
        font.getData().setScale(0.5f);

        atlas = assetManager.get("ui/main.atlas", TextureAtlas.class);
    }

    private void openShop() {
        core.getInputMultiplexer().removeProcessor(core.getInputManager());
        shopIsOpen = true;
        stage.addActor(shopPane);
    }

    private void closeShop() {
        core.getInputMultiplexer().addProcessor(core.getInputManager());
        shopIsOpen = false;
        shopPane.remove();
    }

    private void setUpShop() {
        addRandomCard();
        addRandomCard();
        addRandomRuleCard();

        shopCard1 = new TextButton("", cardStyle);
        shopCard1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(core.getGameData().getTokens() >= shopCards.get(0).getCost() && handCards.size < 6) {
                    core.getGameData().removeTokens(shopCards.get(0).getCost());
                    handCards.add(shopCards.removeIndex(0));
                    addRandomCard();
                    callShopUpdate = true;
                    callHandUpdate = true;
                }
            }
        });
        shopCard2 = new TextButton("", cardStyle);
        shopCard2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(core.getGameData().getTokens() >= shopCards.get(1).getCost() && handCards.size < 6) {
                    core.getGameData().removeTokens(shopCards.get(1).getCost());
                    handCards.add(shopCards.removeIndex(1));
                    addRandomCard();
                    callShopUpdate = true;
                    callHandUpdate = true;
                }
            }
        });
        shopCard3 = new TextButton("", cardStyle);
        shopCard3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(core.getGameData().getTokens() >= shopCards.get(2).getCost() && handCards.size < 6) {
                    core.getGameData().removeTokens(shopCards.get(2).getCost());
                    handCards.add(shopCards.removeIndex(2));
                    addRandomRuleCard();
                    callShopUpdate = true;
                    callHandUpdate = true;
                }
            }
        });

        shopPane.add(shopCard1).size(288/4f, 372/4f).padRight(2);
        shopPane.add(shopCard2).size(288/4f, 372/4f);
        shopPane.add(shopCard3).size(288/4f, 372/4f).padLeft(2);

        callShopUpdate = true;
        callHandUpdate = true;
    }

    private void updateShop() {
        if(callShopUpdate) {
            callShopUpdate = false;
            shopCard1.setText(shopCards.get(0).getDescription() + "\n\n" + shopCards.get(0).getCost());
            shopCard2.setText(shopCards.get(1).getDescription() + "\n\n" + shopCards.get(1).getCost());
            shopCard3.setText(shopCards.get(2).getDescription() + "\n\n" + shopCards.get(2).getCost());
            shopCard3.setStyle(shopCards.get(2) instanceof CardTypes.SpecialCardType ? goldCardStyle : ruleCardStyle);
        }
    }

    private void setUpHand() {
        handCard1 = new TextButton("", cardStyle); handCard1.setSize(288/4f, 372/4f); handCard1.getLabel().setAlignment(Align.top);
        handCard1.setPosition(Core.GAMEW/2f - handCard1.getWidth()/2 - (handCard1.getWidth()/3) * 2.5f, 0 - handCard1.getHeight()/1.8f);
        handCard1.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(handCards.size > 0) {
                    System.out.println(handIsOpen);
                    engine.getSystem(CardHandleSystem.class).activate(handCards.get(0));
                    handCards.removeIndex(0);
                    callHandUpdate = true;
                }
            }
        });
        handCard2 = new TextButton("", cardStyle); handCard2.setSize(288/4f, 372/4f); handCard2.getLabel().setAlignment(Align.top);
        handCard2.setPosition(Core.GAMEW/2f - handCard2.getWidth()/2 - (handCard2.getWidth()/3) * 1.5f, 0 - handCard2.getHeight()/1.8f);
        handCard2.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(handCards.size > 1) {
                    engine.getSystem(CardHandleSystem.class).activate(handCards.get(1));
                    handCards.removeIndex(1);
                    callHandUpdate = true;
                }
            }
        });
        handCard3 = new TextButton("", cardStyle); handCard3.setSize(288/4f, 372/4f); handCard3.getLabel().setAlignment(Align.top);
        handCard3.setPosition(Core.GAMEW/2f - handCard3.getWidth()/2 - (handCard3.getWidth()/3) * 0.5f, 0 - handCard3.getHeight()/1.8f);
        handCard3.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(handCards.size > 2) {
                    engine.getSystem(CardHandleSystem.class).activate(handCards.get(2));
                    handCards.removeIndex(2);
                    callHandUpdate = true;
                }
            }
        });
        handCard4 = new TextButton("", cardStyle); handCard4.setSize(288/4f, 372/4f); handCard4.getLabel().setAlignment(Align.top);
        handCard4.setPosition(Core.GAMEW/2f - handCard4.getWidth()/2 + (handCard4.getWidth()/3) * 0.5f, 0 - handCard4.getHeight()/1.8f);
        handCard4.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(handCards.size > 3) {
                    engine.getSystem(CardHandleSystem.class).activate(handCards.get(3));
                    handCards.removeIndex(3);
                    callHandUpdate = true;
                }
            }
        });
        handCard5 = new TextButton("", cardStyle); handCard5.setSize(288/4f, 372/4f); handCard5.getLabel().setAlignment(Align.top);
        handCard5.setPosition(Core.GAMEW/2f - handCard5.getWidth()/2 + (handCard5.getWidth()/3) * 1.5f, 0 - handCard5.getHeight()/1.8f);
        handCard5.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(handCards.size > 4) {
                    engine.getSystem(CardHandleSystem.class).activate(handCards.get(4));
                    handCards.removeIndex(4);
                    callHandUpdate = true;
                }
            }
        });
        handCard6 = new TextButton("", cardStyle); handCard6.setSize(288/4f, 372/4f); handCard6.getLabel().setAlignment(Align.top);
        handCard6.setPosition(Core.GAMEW/2f - handCard6.getWidth()/2 + (handCard6.getWidth()/3) * 2.5f, 0 - handCard6.getHeight()/1.8f);
        handCard6.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(handCards.size > 5) {
                    engine.getSystem(CardHandleSystem.class).activate(handCards.get(5));
                    handCards.removeIndex(5);
                    callHandUpdate = true;
                }
            }
        });

        stage.addActor(handCard1);
        stage.addActor(handCard2);
        stage.addActor(handCard3);
        stage.addActor(handCard4);
        stage.addActor(handCard5);
        stage.addActor(handCard6);

        handHiddenButton = new TextButton(" ", handHiddenStyle);
        handHiddenButton.setSize(handHiddenButton.getWidth()/4, handHiddenButton.getHeight()/4);
        handHiddenButton.setPosition(0, 0);
        handHiddenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!handIsHidden) {
                    hideHand();
                } else {
                    showHand();
                }
            }
        });
        stage.addActor(handHiddenButton);
    }

    private void updateHand() {
        Vector3 mouse = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        int HSMCAmount = 6 - handCards.size;

        if(mouse.y <= 148/4f && !handIsOpen) {
            handIsOpen = true;
            handCard1.addAction(moveTo(Core.GAMEW/2f - handCard1.getWidth() * 3 + HSMCAmount * handCard1.getWidth()/2, 0 - handCard1.getHeight()/6, 0.5f));
            handCard2.addAction(moveTo(Core.GAMEW/2f - handCard2.getWidth() * 2 + HSMCAmount * handCard2.getWidth()/2, 0 - handCard2.getHeight()/6, 0.5f));
            handCard3.addAction(moveTo(Core.GAMEW/2f - handCard3.getWidth() * 1 + HSMCAmount * handCard3.getWidth()/2, 0 - handCard3.getHeight()/6, 0.5f));
            handCard4.addAction(moveTo(Core.GAMEW/2f + handCard4.getWidth() * 0 + HSMCAmount * handCard4.getWidth()/2, 0 - handCard4.getHeight()/6, 0.5f));
            handCard5.addAction(moveTo(Core.GAMEW/2f + handCard5.getWidth() * 1 + HSMCAmount * handCard5.getWidth()/2, 0 - handCard5.getHeight()/6, 0.5f));
            handCard6.addAction(moveTo(Core.GAMEW/2f + handCard6.getWidth() * 2 + HSMCAmount * handCard6.getWidth()/2, 0 - handCard6.getHeight()/6, 0.5f));
        }
        if(mouse.y >= 300/4f && handIsOpen){
            handIsOpen = false;
            handCard1.addAction(moveTo(Core.GAMEW/2f - handCard1.getWidth()/2 - (handCard1.getWidth()/3) * 2.5f + HSMCAmount * handCard1.getWidth()/6, 0 - handCard1.getHeight()/1.8f, 0.2f));
            handCard2.addAction(moveTo(Core.GAMEW/2f - handCard2.getWidth()/2 - (handCard2.getWidth()/3) * 1.5f + HSMCAmount * handCard2.getWidth()/6, 0 - handCard2.getHeight()/1.8f, 0.2f));
            handCard3.addAction(moveTo(Core.GAMEW/2f - handCard3.getWidth()/2 - (handCard3.getWidth()/3) * 0.5f + HSMCAmount * handCard3.getWidth()/6, 0 - handCard3.getHeight()/1.8f, 0.2f));
            handCard4.addAction(moveTo(Core.GAMEW/2f - handCard4.getWidth()/2 + (handCard4.getWidth()/3) * 0.5f + HSMCAmount * handCard4.getWidth()/6, 0 - handCard4.getHeight()/1.8f, 0.2f));
            handCard5.addAction(moveTo(Core.GAMEW/2f - handCard5.getWidth()/2 + (handCard5.getWidth()/3) * 1.5f + HSMCAmount * handCard5.getWidth()/6, 0 - handCard5.getHeight()/1.8f, 0.2f));
            handCard6.addAction(moveTo(Core.GAMEW/2f - handCard6.getWidth()/2 + (handCard6.getWidth()/3) * 2.5f + HSMCAmount * handCard6.getWidth()/6, 0 - handCard6.getHeight()/1.8f, 0.2f));
        }

        if(callHandUpdate) {
            callHandUpdate = false;

            handCard1.setStyle(blankButtonStyle); handCard1.setText("");
            handCard2.setStyle(blankButtonStyle); handCard2.setText("");
            handCard3.setStyle(blankButtonStyle); handCard3.setText("");
            handCard4.setStyle(blankButtonStyle); handCard4.setText("");
            handCard5.setStyle(blankButtonStyle); handCard5.setText("");
            handCard6.setStyle(blankButtonStyle); handCard6.setText("");

            if(handCards.size > 5) {
                handCard6.setText(handCards.get(5).getDescription());
                handCard6.setStyle(handCards.get(5) instanceof CardTypes.SpecialCardType ? goldCardStyle : handCards.get(5) instanceof CardTypes.RuleCardType ? ruleCardStyle : cardStyle);
            }
            if(handCards.size > 4) {
                handCard5.setText(handCards.get(4).getDescription());
                handCard5.setStyle(handCards.get(4) instanceof CardTypes.SpecialCardType ? goldCardStyle : handCards.get(4) instanceof CardTypes.RuleCardType ? ruleCardStyle : cardStyle);
            }
            if(handCards.size > 3) {
                handCard4.setText(handCards.get(3).getDescription());
                handCard4.setStyle(handCards.get(3) instanceof CardTypes.SpecialCardType ? goldCardStyle : handCards.get(3) instanceof CardTypes.RuleCardType ? ruleCardStyle : cardStyle);
            }
            if(handCards.size > 2) {
                handCard3.setText(handCards.get(2).getDescription());
                handCard3.setStyle(handCards.get(2) instanceof CardTypes.SpecialCardType ? goldCardStyle : handCards.get(2) instanceof CardTypes.RuleCardType ? ruleCardStyle : cardStyle);
            }
            if(handCards.size > 1) {
                handCard2.setText(handCards.get(1).getDescription());
                handCard2.setStyle(handCards.get(1) instanceof CardTypes.SpecialCardType ? goldCardStyle : handCards.get(1) instanceof CardTypes.RuleCardType ? ruleCardStyle : cardStyle);
            }
            if(handCards.size > 0) {
                handCard1.setText(handCards.get(0).getDescription());
                handCard1.setStyle(handCards.get(0) instanceof CardTypes.SpecialCardType ? goldCardStyle : handCards.get(0) instanceof CardTypes.RuleCardType ? ruleCardStyle : cardStyle);
            }

            if(handIsOpen) {
                handCard1.addAction(moveTo(Core.GAMEW/2f - handCard1.getWidth() * 3 + HSMCAmount * handCard1.getWidth()/2, 0 - handCard1.getHeight()/6, 0.5f));
                handCard2.addAction(moveTo(Core.GAMEW/2f - handCard2.getWidth() * 2 + HSMCAmount * handCard2.getWidth()/2, 0 - handCard2.getHeight()/6, 0.5f));
                handCard3.addAction(moveTo(Core.GAMEW/2f - handCard3.getWidth() * 1 + HSMCAmount * handCard3.getWidth()/2, 0 - handCard3.getHeight()/6, 0.5f));
                handCard4.addAction(moveTo(Core.GAMEW/2f + handCard4.getWidth() * 0 + HSMCAmount * handCard4.getWidth()/2, 0 - handCard4.getHeight()/6, 0.5f));
                handCard5.addAction(moveTo(Core.GAMEW/2f + handCard5.getWidth() * 1 + HSMCAmount * handCard5.getWidth()/2, 0 - handCard5.getHeight()/6, 0.5f));
                handCard6.addAction(moveTo(Core.GAMEW/2f + handCard6.getWidth() * 2 + HSMCAmount * handCard6.getWidth()/2, 0 - handCard6.getHeight()/6, 0.5f));
            } else {
                handCard1.addAction(moveTo(Core.GAMEW/2f - handCard1.getWidth()/2 - (handCard1.getWidth()/3) * 2.5f + HSMCAmount * handCard1.getWidth()/6, 0 - handCard1.getHeight()/1.8f, 0.2f));
                handCard2.addAction(moveTo(Core.GAMEW/2f - handCard2.getWidth()/2 - (handCard2.getWidth()/3) * 1.5f + HSMCAmount * handCard2.getWidth()/6, 0 - handCard2.getHeight()/1.8f, 0.2f));
                handCard3.addAction(moveTo(Core.GAMEW/2f - handCard3.getWidth()/2 - (handCard3.getWidth()/3) * 0.5f + HSMCAmount * handCard3.getWidth()/6, 0 - handCard3.getHeight()/1.8f, 0.2f));
                handCard4.addAction(moveTo(Core.GAMEW/2f - handCard4.getWidth()/2 + (handCard4.getWidth()/3) * 0.5f + HSMCAmount * handCard4.getWidth()/6, 0 - handCard4.getHeight()/1.8f, 0.2f));
                handCard5.addAction(moveTo(Core.GAMEW/2f - handCard5.getWidth()/2 + (handCard5.getWidth()/3) * 1.5f + HSMCAmount * handCard5.getWidth()/6, 0 - handCard5.getHeight()/1.8f, 0.2f));
                handCard6.addAction(moveTo(Core.GAMEW/2f - handCard6.getWidth()/2 + (handCard6.getWidth()/3) * 2.5f + HSMCAmount * handCard6.getWidth()/6, 0 - handCard6.getHeight()/1.8f, 0.2f));
            }
        }
    }

    private void hideHand() {
        handHiddenButton.setChecked(true);
        handCard1.setVisible(false);
        handCard2.setVisible(false);
        handCard3.setVisible(false);
        handCard4.setVisible(false);
        handCard5.setVisible(false);
        handCard6.setVisible(false);
        handIsHidden = true;
    }

    private void showHand() {
        handHiddenButton.setChecked(false);
        handCard1.setVisible(true);
        handCard2.setVisible(true);
        handCard3.setVisible(true);
        handCard4.setVisible(true);
        handCard5.setVisible(true);
        handCard6.setVisible(true);
        handIsHidden = false;
    }

    private void addRandomCard() {
        CardTypes.EventCardType randomEventCard = CardTypes.EventCardType.values()[random(0, CardTypes.EventCardType.values().length-1)];
        shopCards.insert(0, randomEventCard);
    }

    private void addRandomRuleCard() {
        int randomNumber = random(0, 10);
        CardTypes.RuleCardType randomRuleCard = CardTypes.RuleCardType.values()[random(0, CardTypes.RuleCardType.values().length-1)];
        CardTypes.SpecialCardType goldCard = CardTypes.SpecialCardType.values()[random(0, CardTypes.SpecialCardType.values().length-1)];
        //shopCards.add(randomNumber != 1 ? goldCard : randomRuleCard);
        shopCards.add(goldCard);
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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.SHIFT_LEFT) {
            if(!handIsHidden) {
                hideHand();
            } else {
                showHand();
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
