package vsp.trongame.app.controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import vsp.trongame.app.model.gamemanager.ITronModel;

/**
 * Implements the interface ITronController
 */
public class TronController implements ITronController {

    private ITronModel model;

    @Override
    public void initKeyEventHandler(Scene scene) {

    }

    @Override
    public void startGame(ActionEvent event){

    }

    @Override
    public void handleKeyEvent(KeyEvent event){

    }

    @Override
    public ITronModel getModel(){
        return null;
    }

    public void setModel(ITronModel model){
        this.model = model;
    }
}
