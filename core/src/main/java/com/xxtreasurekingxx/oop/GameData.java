package com.xxtreasurekingxx.oop;

public class GameData {
    private final Core core;
    private int score;

    public GameData(final Core core) {
        this.core = core;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void removeScore(int score) {
        this.score -= score;
    }

    public int getScore() {
        return score;
    }
}
