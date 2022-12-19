package vsp.trongame.app.controller;

import javafx.scene.input.KeyEvent;
import vsp.trongame.app.model.ITronModel;

/**
 * Implements the interface ITronController
 */
public class TronController implements ITronController {

    private ITronModel model;

    @Override
    public void playGame(int id, int playerCount){
        model.playGame(id, playerCount);
    }

    @Override
    public void handleKeyEvent(int id, KeyEvent event){
    }

    @Override
    public void initialize(ITronModel model){
        this.model = model;
    }
}
