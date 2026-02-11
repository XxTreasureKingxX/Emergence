package com.xxtreasurekingxx.oop;

public class GameData {
    private final Core core;
    private int score;
    private int tokens;

    public GameData(final Core core) {
        this.core = core;
        addTokens(3);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void removeScore(int score) {
        this.score -= score;
    }

    public void addTokens(int tokens) {
        this.tokens += tokens;
    }

    public void removeTokens(int tokens) {
        this.tokens -= tokens;
    }

    public int getScore() {
        return score;
    }

    public int getTokens() {
        return tokens;
    }
}
