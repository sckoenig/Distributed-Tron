package vsp.trongame.app.view.overlays;

import vsp.trongame.app.controller.ITronController;

public abstract class Overlay {

    private ITronController controller;

    public void setController(ITronController controller){
        this.controller = controller;
    }
    public ITronController getController(){
        return this.controller;
    }

    public abstract void init();

}
