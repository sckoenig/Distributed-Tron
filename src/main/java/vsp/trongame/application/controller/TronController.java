package vsp.trongame.application.controller;

import javafx.scene.input.KeyEvent;
import vsp.trongame.application.model.ITronModel;
import vsp.trongame.application.model.IUpdateListener;

/**
 * Serves as the main controller.
 */
public class TronController implements ITronController {

    private ITronModel model;

    @Override
    public void playGame(IUpdateListener listener, int playerCount){
        model.playGame(listener, playerCount);
    }

    @Override
    public void handleKeyEvent(int id, KeyEvent event){
        model.handleSteerEvent(id, event.getCode().getChar());
    }

    @Override
    public void initialize(ITronModel model){
        this.model = model;
    }
}
