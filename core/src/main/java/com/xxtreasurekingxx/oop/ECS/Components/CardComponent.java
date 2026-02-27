package com.xxtreasurekingxx.oop.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.oop.ECS.Systems.CardHandleSystem;

import javax.smartcardio.Card;
import java.util.function.Consumer;

public class CardComponent implements Component, Pool.Poolable {

    @Override
    public void reset() {
    }
//
//    public interface CardType {
//        void callMethod(CardHandleSystem system);
//        int getCost();
//        String getDescription();
//        CardType[] getValues();
//    }
//
//    public enum EventCardType implements CardType {
//        PULL(5,  "Attracts all planets\n to the center of the board", CardHandleSystem::PULL),
//        SHRINK(5,  "The biggest planet\n on the board will\n shrink by 50%", CardHandleSystem::SHRINK),
//        REMOVE(1,  "Removes a random\n planet from the board", CardHandleSystem::REMOVE);
//
//        private final Consumer<CardHandleSystem> method;
//        private final int cost;
//        private final String description;
//        private static final EventCardType[] cardValues = values();
//
//        EventCardType(final int cost, final String description, final Consumer<CardHandleSystem> method) {
//            this.cost = cost;
//            this.description = description;
//            this.method = method;
//        }
//        public void callMethod(CardHandleSystem cardSystem) {
//            method.accept(cardSystem);
//        }
//        public int getCost() {
//            return cost;
//        }
//        public String getDescription() {
//            return description;
//        }
//        public EventCardType[] getValues() {
//            return cardValues;
//        }
//    }
//
//    public enum RuleCardType implements CardType{
//        BH75(1,  "Black holes now spawn\n randomly, and planets are\n 75% of their original size", CardHandleSystem::BH75);
//        //HAND(1,  "Cards can no longer\n be deleted and\n ...", CardHandleSystem::);
//
//        private final Consumer<CardHandleSystem> method;
//        private final int cost;
//        private final String description;
//        private static final RuleCardType[] cardValues = values();
//
//        RuleCardType(final int cost, final String description, final Consumer<CardHandleSystem> method) {
//            this.cost = cost;
//            this.description = description;
//            this.method = method;
//        }
//        public void callMethod(CardHandleSystem cardSystem) {
//            method.accept(cardSystem);
//        }
//        public int getCost() {
//            return cost;
//        }
//        public String getDescription() {
//            return description;
//        }
//        public RuleCardType[] getValues() {
//            return cardValues;
//        }
//    }
//
//    public enum SpecialCardType implements CardType{
//        GOLD(1, "Spawns in a gold planet", CardHandleSystem::goldCallMethod);
//        //REMOVE(1, "Removes every planet\n of the lowest tier from\n the board");
//
//        private final Consumer<CardHandleSystem> method;
//        private final int cost;
//        private final String description;
//        private static final SpecialCardType[] cardValues = values();
//
//        SpecialCardType(final int cost, final String description, final Consumer<CardHandleSystem> method) {
//            this.cost = cost;
//            this.description = description;
//            this.method = method;
//        }
//        public void callMethod(CardHandleSystem cardSystem) {
//            System.out.println("running event trying");
//            method.accept(cardSystem);
//        }
//        public int getCost() {
//            return cost;
//        }
//        public String getDescription() {
//            return description;
//        }
//        public SpecialCardType[] getValues() {
//            return cardValues;
//        }
//    }
}
