package vsp.trongame.app.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import vsp.trongame.app.model.ITronModel;

/**
 * Implements the interface ITronController
 */
public class TronController implements ITronController {

    private ITronModel model;

    @Override
    public void initKeyEventHandler(Scene scene) {

    }

    @Override
    public void startGame(int playerCount){
        model.initializeGame(playerCount);
    }

    @Override
    public void handleKeyEvent(KeyEvent event){}

    @Override
    public void setModel(ITronModel model){
        this.model = model;
    }
}
