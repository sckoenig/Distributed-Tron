package vsp.trongame.app.controller;

import javafx.scene.input.KeyEvent;
import vsp.trongame.app.model.ITronModel;

/**
 * Serves as the main controller.
 */
public class TronController implements ITronController {

    private ITronModel model;

    @Override
    public void playGame(int id, int playerCount){
        model.playGame(id, playerCount);
    }

    @Override
    public void handleKeyEvent(int id, KeyEvent event){
        model.handleSteerEvent(id, event.getCode());
    }

    @Override
    public void initialize(ITronModel model){
        this.model = model;
    }
}
