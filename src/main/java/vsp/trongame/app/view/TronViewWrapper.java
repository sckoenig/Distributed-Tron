package vsp.trongame.app.view;

import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.model.IModelUpdateListener;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.view.listener.TronModelUpdateListener;
import vsp.trongame.app.view.overlays.*;

import java.io.IOException;
import java.util.*;

/**
 * Represents a wrapper for the {@link ITronView}. Builds the view and the model listener.
 */
public class TronViewWrapper implements IViewWrapper {

    private ITronView mainView;
    private TronModelUpdateListener listener;

    @Override
    public void buildView(ITronModel model, ITronController mainController, int height, int width, int defaultPlayerCount,
                          Map<String, String> mapping) throws IOException {

        this.mainView = new TronView();
        MenuOverlay menuOverlay;
        CountdownOverlay countdownOverlay = null;
        EndingOverlay endingOverlay = null;

        Parent root;
        FXMLLoader loader;
        for (Map.Entry<String, String> overlay : mapping.entrySet()) {

            String fxml = overlay.getValue();
            String identifier = overlay.getKey();
            loader = new FXMLLoader(getClass().getResource(fxml));
            root = loader.load();
            root.minHeight(height);
            root.minWidth(width);
            mainView.registerOverlay(identifier, root);

            switch (overlay.getKey()) {
                case MenuOverlay.IDENTIFIER -> {
                    menuOverlay = loader.getController();
                    menuOverlay.initialize(mainController, defaultPlayerCount);
                }
                case CountdownOverlay.IDENTIFIER -> countdownOverlay = loader.getController();
                case EndingOverlay.IDENTIFIER -> endingOverlay = loader.getController();
                default -> {
                }
            }
        }
        mainView.clear(); // hide all overlays

        this.listener = new TronModelUpdateListener();
        this.listener.initialize(mainView, countdownOverlay, endingOverlay, mainController);
        model.registerUpdateListener(listener);
    }

    @Override
    public Scene getScene() {
        return mainView.getScene();
    }

    @Override
    public IModelUpdateListener getListener() {
        return this.listener;
    }


}

