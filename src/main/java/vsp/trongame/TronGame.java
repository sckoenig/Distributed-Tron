package vsp.trongame;

import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.controller.ITronControllerFactory;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.ITronModelFactory;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanagement.*;
import vsp.trongame.app.view.ITronViewFactory;
import vsp.trongame.app.view.overlays.MenuOverlay;
import vsp.trongame.app.view.overlays.Overlay;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import static vsp.trongame.app.model.datatypes.GameModus.LOCAL;

public class TronGame extends Application {

    private static final String CONFIG_FILE = "tronConfig.properties";
    private static final Map<ModelState, String> STATE_VIEW_MAPPING = new EnumMap<>(Map.of(ModelState.MENU, "menuOverlay.fxml", ModelState.WAITING, "waitingOverlay.fxml",
            ModelState.COUNTDOWN, "countdownOverlay.fxml",
            ModelState.ENDING, "endingOverlay.fxml"));

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

        /* assemble */
        loadViewOverlays(tronView, tronController, tronModel, config);
        tronView.setObservableState(tronModel.getObservableModel().getObservableState());
        tronView.setObservablePlayers(tronModel.getObservableModel().getObservablePlayers());

        tronController.setModel(tronModel);
        tronController.initKeyEventHandler(tronView.getScene());

        boolean singleView;
        if (modus != LOCAL) singleView = false;
        else singleView = true;

        /* init */
        tronView.init();
        tronModel.init(singleView, config);

        /* open stage */
        stage.setTitle("Tron");
        stage.setScene(tronView.getScene());
        stage.show();
    }

    public void loadViewOverlays(ITronView view, ITronController controller, ITronModel model, Config config) throws IOException {

        // TODO: add height & width from config to roots

        Parent root;
        FXMLLoader loader;

        for (Map.Entry<ModelState, String> overlay : STATE_VIEW_MAPPING.entrySet()) {

            String fxml = overlay.getValue();
            ModelState state = overlay.getKey();
            loader = new FXMLLoader(getClass().getResource(fxml));
            root = loader.load();
            view.registerOverlay(state.toString(), root);

            switch (overlay.getKey()) {
                case MENU -> {
                    MenuOverlay menuOverlay = loader.getController();
                    menuOverlay.setController(controller);
                    menuOverlay.setDefaultPlayerCount(2); //TODO: default from config
                }
                default -> {
                    Overlay loadedOverlay = loader.getController();
                    loadedOverlay.init(model.getObservableModel());
                }
            }
        }
    }
}