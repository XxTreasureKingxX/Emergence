package com.xxtreasurekingxx.oop.ECS;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.oop.Core;
import com.xxtreasurekingxx.oop.ECS.Components.*;
import com.xxtreasurekingxx.oop.ECS.Systems.*;
import com.xxtreasurekingxx.oop.World.ObjectType;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.xxtreasurekingxx.oop.World.AnimationType.RING;

public class ECSEngine extends PooledEngine {
    private final Core core;
    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final FitViewport viewport;

    private final PhysicsSystem physicsSystem;
    private final BodyHandleSystem bodyHandleSystem;
    private final CollisionSystem collisionSystem;
    private final GameUISystem gameUISystem;
    private final RenderSystem renderSystem;
    private final ObjectAntiSpawnRadiusSystem OASRSystem;
    private final CardHandleSystem cardSystem;

    public int blackHoleCounter = 0;
    public Vector2 blackHolePosition;

    public static final ComponentMapper<B2DComponent> b2dCmpMpr = ComponentMapper.getFor(B2DComponent.class);
    public static final ComponentMapper<AnimationComponent> aniCmpMpr = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<ObjectComponent> objCmpMpr = ComponentMapper.getFor(ObjectComponent.class);
    public static final ComponentMapper<BlackHoleComponent> blkHoleCmpMpr = ComponentMapper.getFor(BlackHoleComponent.class);
    public static final ComponentMapper<ActorComponent> actCmpMpr = ComponentMapper.getFor(ActorComponent.class);
    public static final ComponentMapper<ParticleComponent> ptclCmpMpr = ComponentMapper.getFor(ParticleComponent.class);

    public ECSEngine(final Core core, final FitViewport viewport) {
        super();
        this.core = core;
        this.viewport = viewport;
        batch = core.getBatch();
        assetManager = core.getAssetManager();

        //Systems
        physicsSystem = new PhysicsSystem();
        bodyHandleSystem = new BodyHandleSystem(this, physicsSystem.getWorld());
        collisionSystem = new CollisionSystem(core, this, physicsSystem.getContactListener());
        cardSystem = new CardHandleSystem(this);
        gameUISystem = new GameUISystem(core, viewport, this);
        OASRSystem = new ObjectAntiSpawnRadiusSystem(core, batch, viewport);
        renderSystem = new RenderSystem(core, batch, physicsSystem.getWorld(), viewport);

        this.addSystem(new ParticleSystem(core));
        this.addSystem(new SpawnSystem(core, this));
        this.addSystem(new AnimationSystem());
        this.addSystem(new BlackHoleSystem(this));
        this.addSystem(physicsSystem);
        this.addSystem(collisionSystem);
        this.addSystem(new EvolutionSystem(this));
        this.addSystem(cardSystem);
        this.addSystem(gameUISystem);
        this.addSystem(bodyHandleSystem);
        this.addSystem(new BackgroundRenderSystem(core, batch, viewport));
        this.addSystem(OASRSystem);
        this.addSystem(renderSystem);
    }

    public void createObject(final Vector2 pos, final ObjectType type, final boolean squareShape, final int startingExp, final boolean spawnParticle) {
        Entity object = this.createEntity();

        //B2DComponent
        //B2DComponent b2DComponent = this.createComponent(B2DComponent.class);
        B2DComponent b2DComponent = new B2DComponent();
        b2DComponent.renderPosition = new Vector2(pos);
        b2DComponent.width = type.getAnimationType().getDiameter();
        b2DComponent.height = type.getAnimationType().getDiameter();
        b2DComponent.shouldCollide = true;
        b2DComponent.angle = random(0, 360);

        b2DComponent.needsBody = true;
        if(squareShape) {
            b2DComponent.needsSquareBody = true;
            b2DComponent.needsBody = false;
        }
        object.add(b2DComponent);

        //AnimationComponent
        //AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.type = type.getAnimationType();
        object.add(animationComponent);

        //ObjectComponent
        //ObjectComponent objectComponent = this.createComponent(ObjectComponent.class);
        ObjectComponent objectComponent = new ObjectComponent();
        objectComponent.type = type;
        objectComponent.exp = startingExp;
        object.add(objectComponent);

        //ActorComponent
        //ActorComponent actorComponent = this.createComponent(ActorComponent.class);
        ActorComponent actorComponent = new ActorComponent();
        actorComponent.needsActor = squareShape;
        if(type == ObjectType.ANOMALY) {
            actorComponent.needsActor = false;
        }
        object.add(actorComponent);

        //ParticleComponent
        //ParticleComponent particleComponent = this.createComponent(ParticleComponent.class);
        ParticleComponent particleComponent = new ParticleComponent();
        particleComponent.particles = new Array<>();
        particleComponent.position = pos;
        particleComponent.type = ParticleComponent.ParticlesType.EXPLOSION;
        particleComponent.scale = 0.5f;
        object.add(particleComponent);

        if(spawnParticle) {
            this.getSystem(ParticleSystem.class).addParticle(particleComponent, pos.x, pos.y);
        }

        this.addEntity(object);
    }

    public void createCollisionRing(final ObjectType type, final Vector2 position) {
        final Entity entity = this.createEntity();
        System.out.println("creating collision ring");
        //B2DComponent
        //B2DComponent b2DComponent = this.createComponent(B2DComponent.class);
        B2DComponent b2DComponent = new B2DComponent();
        b2DComponent.renderPosition = new Vector2(position);
        b2DComponent.width = type.getAnimationType().getDiameter();
        b2DComponent.height = type.getAnimationType().getDiameter();
        entity.add(b2DComponent);

        //AnimationComponent
        //AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.type = RING;
        entity.add(animationComponent);

        this.addEntity(entity);
    }

    public void createGameBorder() {
        final Entity border = this.createEntity();

        //B2DComponent b2DComponent = this.createComponent(B2DComponent.class);
        B2DComponent b2DComponent = new B2DComponent();
        b2DComponent.renderPosition = new Vector2(Core.GAMEW/2f, Core.GAMEH/2f);
        b2DComponent.height = Core.BORDER_SIZE;
        b2DComponent.width = b2DComponent.height;
        b2DComponent.isBorder = true;
        b2DComponent.shouldCollide = true;
        border.add(b2DComponent);

        this.addEntity(border);
    }

    public void resize(int width, int height) {
        OASRSystem.resize(width, height);
        viewport.update(width, height);
    }

    public void hide() {
        core.getInputMultiplexer().removeProcessor(gameUISystem.getStage());
    }
}
