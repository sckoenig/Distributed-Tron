package vsp.trongame;

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
import vsp.trongame.app.view.IViewWrapper;
import vsp.trongame.app.view.IViewWrapperFactory;
import vsp.trongame.app.view.overlays.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static vsp.trongame.app.model.datatypes.GameModus.LOCAL;

public class TronGame extends Application {

    private static final Map<ModelState, String> STATE_VIEW_MAPPING = new EnumMap<>(Map.of(
            ModelState.MENU, "menuOverlay.fxml",
            ModelState.WAITING, "waitingOverlay.fxml",
            ModelState.COUNTDOWN, "countdownOverlay.fxml",
            ModelState.ENDING, "endingOverlay.fxml"));

    private static final int MODEL_THREAD_COUNT = 2; // model has max two tasks simultaneously
    private ExecutorService modelExecutor; // reference for shutting down on application stop

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        /* BOOT APP */
        this.modelExecutor = Executors.newFixedThreadPool(MODEL_THREAD_COUNT);
        Config config = new Config();
        GameModus modus = LOCAL; //TODO get from Config when Config is implemented
        boolean singleView = modus == LOCAL? true : false;

        /* components */
        IViewWrapper tronView = IViewWrapperFactory.getViewWrapper(modus);
        ITronController tronController = ITronControllerFactory.getTronController(modus);
        ITronModel tronModel = ITronModelFactory.getTronModel(modus);

        /* assemble */
        tronController.initialize(tronModel);
        tronModel.initialize(modus, modelExecutor, singleView, config);
        initializeViewWithFxml(tronView, tronController, tronModel, config);

        /* open stage */
        stage.setTitle("Tron");
        stage.setScene(tronView.getScene());
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        this.modelExecutor.shutdownNow(); //stop threads
    }

    public void initializeViewWithFxml(IViewWrapper view, ITronController controller, ITronModel model, Config config) throws IOException {

        // TODO: add height & width from config to roots

        Parent root;
        FXMLLoader loader;

        MenuOverlay menuOverlay = null;
        WaitingOverlay waitingOverlay = null;
        CountdownOverlay countdownOverlay = null;
        EndingOverlay endingOverlay = null;

        for (Map.Entry<ModelState, String> overlay : STATE_VIEW_MAPPING.entrySet()) {

            String fxml = overlay.getValue();
            ModelState state = overlay.getKey();
            loader = new FXMLLoader(getClass().getResource(fxml));
            root = loader.load();
            root.minHeight(600);
            root.minWidth(750);
            view.registerOverlay(state.toString(), root);

            switch (overlay.getKey()) {
                case MENU -> menuOverlay = loader.getController();
                case WAITING -> waitingOverlay = loader.getController();
                case COUNTDOWN ->  countdownOverlay = loader.getController();
                case ENDING -> endingOverlay = loader.getController();
                default -> {}
            }
        }
        view.initialize(menuOverlay, waitingOverlay, countdownOverlay, endingOverlay, model, controller, 2);
    }
}