package com.xxtreasurekingxx.oop.ECS.Systems;

import com.badlogic.ashley.core.EntitySystem;
import com.xxtreasurekingxx.oop.CardTypes;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;

public class CardHandleSystem extends EntitySystem {
    private final ECSEngine engine;

    public CardHandleSystem(final ECSEngine engine) {
        this.engine = engine;
    }

    public void activate(CardTypes.CardType type) {
        type.callMethod(this);
    }

    //Event Card Effects
    public void PULL() {

    }
    public void REMOVE() {

    }
    public void SHRINK() {

    }

    //Rule Card Effects
    public void BH75() {

    }

    //Special Card Effects
    public void goldCallMethod() {
        System.out.println("Spawning gold obbject:/????/");
        engine.getSystem(SpawnSystem.class).spawnGoldObject();
    }
}
