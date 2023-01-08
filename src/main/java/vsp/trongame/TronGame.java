package vsp.trongame;

import javafx.application.Application;
import javafx.stage.Stage;
import vsp.trongame.application.controller.ITronController;
import vsp.trongame.application.controller.ITronControllerFactory;
import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.model.gamemanagement.Configuration;
import vsp.trongame.application.model.ITronModel;
import vsp.trongame.application.model.ITronModelFactory;
import vsp.trongame.application.model.gamemanagement.*;
import vsp.trongame.application.view.TronViewBuilder;
import vsp.trongame.applicationstub.model.game.GameCallee;
import vsp.trongame.applicationstub.model.gamemanagement.GameManagerCallee;
import vsp.trongame.applicationstub.model.rest.RESTStub;
import vsp.trongame.applicationstub.view.UpdateListenerCallee;
import vsp.middleware.Middleware;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static vsp.trongame.Modus.*;

public class TronGame extends Application {

    private static final Map<String, String> STATE_VIEW_MAPPING = new HashMap<>(Map.of(
            ModelState.MENU.toString(), "menuOverlay.fxml",
            ModelState.WAITING.toString(), "waitingOverlay.fxml",
            ModelState.COUNTDOWN.toString(), "countdownOverlay.fxml",
            ModelState.ENDING.toString(), "endingOverlay.fxml"));

    public static void main(String[] args) {
        launch();
    }

    private static final int MODEL_THREAD_SIZE = 3;
    private Modus gameModus;

    private ExecutorService modelExecutor;

    @Override
    public void start(Stage stage) throws Exception {

        /* BOOT APP */

        Configuration config = new Configuration();
        gameModus = Modus.valueOf(config.getAttribut("gameMode"));
        boolean singleView = gameModus == LOCAL;
        modelExecutor = Executors.newFixedThreadPool(MODEL_THREAD_SIZE);

        /* components for rpc or rest game */
        if (gameModus != LOCAL) {
            String networkAddress = config.getAttribut(Configuration.NETWORK_ADDRESS);
            String ip = findIpAddress(networkAddress);
            boolean asNameServerHost = Boolean.parseBoolean(config.getAttribut(Configuration.NAME_SERVER_HOST));
            String nameServerAddress = config.getAttribut(Configuration.NAME_SERVER);
            Middleware.getInstance().start(nameServerAddress, asNameServerHost, ip);
            RESTStub.getInstance().setIpAddress(ip);
        }

        /* local components */
        ITronController tronController = ITronControllerFactory.getTronController(Modus.LOCAL);
        ITronModel tronModel = ITronModelFactory.getTronModel(Modus.LOCAL);

        /* assemble */
        tronController.initialize(tronModel);
        tronModel.initialize(config, gameModus, singleView, modelExecutor);
        IUpdateListener listener = TronViewBuilder.buildView(tronController, Integer.parseInt(config.getAttribut(Configuration.HEIGHT)),
                Integer.parseInt(config.getAttribut(Configuration.WIDTH)),
                Integer.parseInt(config.getAttribut(Configuration.DEFAULT_PLAYER_NUMBER)), STATE_VIEW_MAPPING, stage);
        Modus modelModus = gameModus == REST? RPC : gameModus;
        tronModel.initialize(config, modelModus, singleView, modelExecutor);

        /* stubs if not LOCAL */
        if (gameModus == Modus.RPC) createApplicationStub(LOCAL, config, listener, (IGameManager) tronModel);
        if (gameModus == REST) createApplicationStub(REST, config, listener, (IGameManager) tronModel);

        /* open stage */
        stage.show();

    }

    private String findIpAddress(String network) throws SocketException {
        String[] split = network.split("/");
        String ip = split[0];
        short prefix = Short.parseShort(split[1]);

        for(Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces(); eni.hasMoreElements(); ) {
            final NetworkInterface ifc = eni.nextElement();

            List<InterfaceAddress> interfaceAddresses = ifc.getInterfaceAddresses();
            for (InterfaceAddress address : interfaceAddresses) {
                if (address.getNetworkPrefixLength() == prefix) {
                    String interfaceAddress = address.getAddress().getHostAddress();
                    String networkNumsInterface = interfaceAddress.substring(0, interfaceAddress.lastIndexOf("."));
                    String networkNumbsConfig = ip.substring(0, interfaceAddress.lastIndexOf("."));
                    if (networkNumsInterface.equals(networkNumbsConfig)) return interfaceAddress;
                }
            }
        }
        return "";
    }

    private void createApplicationStub(Modus modus, Configuration config, IUpdateListener listener, IGameManager gameManager){
        new GameCallee(modus, config, modelExecutor);
        new UpdateListenerCallee(listener);
        new GameManagerCallee(gameManager);
    }

    /**
     * Executed on pressing close-Button.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        modelExecutor.shutdownNow(); //shutdown any model threads
        if (gameModus != LOCAL) Middleware.getInstance().stop(); //shutdown any middleware threads
        if (gameModus == REST) RESTStub.getInstance().stop();
    }

}