package vsp.trongame;

import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.controller.ITronControllerFactory;
import vsp.trongame.app.model.ITronModelFactory;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanager.*;
import vsp.trongame.app.view.ITronViewFactory;
import vsp.trongame.app.view.overlays.Overlay;

import java.io.IOException;
import java.util.Map;

import static vsp.trongame.app.model.datatypes.GameModus.LOCAL;

public class TronGame extends Application {

    public static final String CONFIG_FILE = "tronConfig.properties";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        /* BOOT APP */

        Config config = new Config();
        GameModus modus = LOCAL; //TODO change to Config when Config is implemented

        /* components */
        ITronView tronView = ITronViewFactory.getTronView(modus, CONFIG_FILE);
        ITronController tronController = ITronControllerFactory.getTronController(modus);
        ITronModel tronModel = ITronModelFactory.getTronModel(modus);

        loadViewOverlays(tronView, tronController);

        /* assemble */
        tronController.setModel(tronModel);
        tronController.initKeyEventHandler(tronView.getScene());
        tronModel.setView(tronView);
        tronModel.setConfig(config);

        /* open stage */
        stage.setTitle("Tron");
        stage.setScene(tronView.getScene());
        stage.show();

    }

    public void loadViewOverlays(ITronView view, ITronController controller) throws IOException {

        // TODO: add height & width from config to roots

        Parent root;
        FXMLLoader loader;

        for(Map.Entry<ModelState, String> overlay : ITronModel.MODEL_STATES.entrySet()){

            String fxml = overlay.getValue();
            ModelState state = overlay.getKey();
            loader = new FXMLLoader(getClass().getResource(fxml));
            root = loader.load();
            view.registerOverlay(state.toString(), root);

            Overlay loadedOverlay = loader.getController();
            loadedOverlay.setController(controller);
            loadedOverlay.init();

        }
    }
}