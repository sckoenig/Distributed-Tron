package vsp.trongame.application.view;

import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import vsp.trongame.Modus;
import vsp.trongame.application.controller.ITronController;
import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.view.listener.UpdateListener;
import vsp.trongame.application.view.overlays.*;

import java.io.IOException;
import java.util.*;

/**
 * Builds the view and the view's listener.
 */
public class TronViewBuilder {

    /**
     * Creates Instances of {@link ITronController} depending on {@link Modus}.
     *
     * @return controller instance.
     */
    public static IUpdateListener buildView(ITronController mainController, int height, int width, int defaultPlayerCount,
                                            Map<String, String> mapping, Stage stage) throws IOException {

        ITronView mainView = new TronView();
        stage.setScene(mainView.getScene());
        UpdateListener listener = new UpdateListener();
        MenuOverlay menuOverlay;
        CountdownOverlay countdownOverlay = null;
        EndingOverlay endingOverlay = null;

        Parent root;
        FXMLLoader loader;
        for (Map.Entry<String, String> overlay : mapping.entrySet()) {

            String fxml = overlay.getValue();
            String identifier = overlay.getKey();
            loader = new FXMLLoader(TronViewBuilder.class.getResource(fxml));
            root = loader.load();
            root.minHeight(height);
            root.minWidth(width);
            mainView.registerOverlay(identifier, root);

            switch (overlay.getKey()) {
                case MenuOverlay.IDENTIFIER -> {
                    menuOverlay = loader.getController();
                    menuOverlay.initialize(mainController, defaultPlayerCount, listener);
                }
                case CountdownOverlay.IDENTIFIER -> countdownOverlay = loader.getController();
                case EndingOverlay.IDENTIFIER -> endingOverlay = loader.getController();
                default -> {
                }
            }
        }

        mainView.init(); //hide all registered overlays
        mainView.showOverlay(MenuOverlay.IDENTIFIER);
        listener.initialize(mainView, countdownOverlay, endingOverlay, mainController);

        return listener;
    }

    private TronViewBuilder(){}
}




