package com.xxtreasurekingxx.oop;

import com.xxtreasurekingxx.oop.ECS.Components.CardComponent;
import com.xxtreasurekingxx.oop.ECS.ECSEngine;
import com.xxtreasurekingxx.oop.ECS.Systems.CardHandleSystem;

import java.util.function.Consumer;

public class CardTypes {

    public interface CardType {
        void callMethod(CardHandleSystem system);
        int getCost();
        String getDescription();
    }

    public enum EventCardType implements CardTypes.CardType {
        PULL(5,  "Attracts all planets\n to the center of the board", CardHandleSystem::PULL),
        SHRINK(5,  "The biggest planet\n on the board will\n shrink by 50%", CardHandleSystem::SHRINK),
        REMOVE(1,  "Removes a random\n planet from the board", CardHandleSystem::REMOVE);

        private final Consumer<CardHandleSystem> method;
        private final int cost;
        private final String description;

        EventCardType(final int cost, final String description, final Consumer<CardHandleSystem> method) {
            this.cost = cost;
            this.description = description;
            this.method = method;
        }
        public void callMethod(CardHandleSystem cardSystem) {
            method.accept(cardSystem);
        }
        public int getCost() {
            return cost;
        }
        public String getDescription() {
            return description;
        }
        public CardTypes.EventCardType[] getValues() {
            return values();
        }
    }

    public enum RuleCardType implements CardTypes.CardType {
        BH75(1,  "Black holes now spawn\n randomly, and planets are\n 75% of their original size", CardHandleSystem::BH75);
        //HAND(1,  "Cards can no longer\n be deleted and\n ...", CardHandleSystem::);

        private final Consumer<CardHandleSystem> method;
        private final int cost;
        private final String description;

        RuleCardType(final int cost, final String description, final Consumer<CardHandleSystem> method) {
            this.cost = cost;
            this.description = description;
            this.method = method;
        }
        public void callMethod(CardHandleSystem cardSystem) {
            method.accept(cardSystem);
        }
        public int getCost() {
            return cost;
        }
        public String getDescription() {
            return description;
        }
        public CardTypes.RuleCardType[] getValues() {
            return values();
        }
    }

    public enum SpecialCardType implements CardTypes.CardType {
        GOLD(1, "Spawns in a gold planet", CardHandleSystem::goldCallMethod);
        //REMOVE(1, "Removes every planet\n of the lowest tier from\n the board");

        private final Consumer<CardHandleSystem> method;
        private final int cost;
        private final String description;

        SpecialCardType(final int cost, final String description, final Consumer<CardHandleSystem> method) {
            this.cost = cost;
            this.description = description;
            this.method = method;
        }
        public void callMethod(CardHandleSystem cardSystem) {
            System.out.println("running event trying");
            method.accept(cardSystem);
        }
        public int getCost() {
            return cost;
        }
        public String getDescription() {
            return description;
        }
        public CardTypes.SpecialCardType[] getValues() {
            return values();
        }
    }
}
