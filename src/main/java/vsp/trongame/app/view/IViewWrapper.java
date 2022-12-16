package vsp.trongame.app.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.view.overlays.CountdownOverlay;
import vsp.trongame.app.view.overlays.EndingOverlay;
import vsp.trongame.app.view.overlays.MenuOverlay;
import vsp.trongame.app.view.overlays.WaitingOverlay;


public interface IViewWrapper {

   void initialize(MenuOverlay menuOverlay, WaitingOverlay waitingOverlay, CountdownOverlay countdownOverlay, EndingOverlay endingOverlay, ITronModel model, ITronController controller, int defaultPlayerCount);

    Scene getScene();

    void registerOverlay(String overlayName, Node node);
}
