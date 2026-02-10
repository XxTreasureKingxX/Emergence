package com.xxtreasurekingxx.oop.Input;

import com.badlogic.gdx.InputProcessor;

public interface InputListener extends InputProcessor {
    void keyPressed(final InputManager manager, final GameKeys key);
    void keyReleased(final InputManager manager, final GameKeys key);
}
