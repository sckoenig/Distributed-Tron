package vsp.trongame.applicationstub.model.game;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.game.IGameFactory;
import vsp.trongame.app.model.datatypes.DirectionChange;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.app.model.gamemanagement.IGameManagerFactory;
import vsp.trongame.app.view.IUpdateListenerFactory;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;
import vsp.trongame.middleware.Middleware;

import java.util.ArrayList;
import java.util.List;

import static vsp.trongame.applicationstub.util.Service.*;

/**
 * Callee Remote Object, that knows the local Instance of {@link IGame} and can call its methods.
 */
public class IGameCallee implements IRemoteObject {

    private final IGame localGame;

    public IGameCallee() {
        this.localGame = IGameFactory.getGame(GameModus.LOCAL); //knows the "real" game

        // can be called from remote
        IRegister middleware = Middleware.getInstance();
        middleware.registerRemoteObject(PREPARE.ordinal(), this);
        middleware.registerRemoteObject(REGISTER.ordinal(), this);
        middleware.registerRemoteObject(HANDLE_STEERS.ordinal(), this);
    }

    @Override
    public void call(int serviceID, int[] intParameters, String... stringParameters) {
        Service service = Service.getByOrdinal(serviceID);
        switch (service){
            case PREPARE -> {
                if (intParameters.length == 1){
                    int playerCount = intParameters[0];
                    localGame.prepare(playerCount);
                }
            }
            case REGISTER -> {
                if (intParameters.length == 2 && stringParameters.length == 2){

                    // Caller Objects that represent the remote Objects that want to register at the local game
                    IGameManager managerCaller = IGameManagerFactory.getGameManager(GameModus.NETWORK);
                    ITronModel.IUpdateListener listenerCaller = IUpdateListenerFactory.getUpdateListener(GameModus.NETWORK);
                    ((ICaller) managerCaller).setRemoteId(stringParameters[0]);
                    ((ICaller) listenerCaller).setRemoteId(stringParameters[1]);

                    localGame.register(managerCaller, listenerCaller, intParameters[0], intParameters[1]);
                }
            }
            case HANDLE_STEERS -> {
                boolean uneven = intParameters.length % 2 != 0; // must be uneven: tickcount + steer(id, direction)
                if (intParameters.length < 13 && intParameters.length > 0 && uneven){
                    int tickCount = intParameters[0];
                    List<Steer> steerList = new ArrayList<>();
                    for(int i = 1; i < intParameters.length; i+=2){
                        Steer steer = new Steer(intParameters[i], DirectionChange.getByOrdinal(intParameters[i+1]));
                        steerList.add(steer);
                    }
                    localGame.handleSteers(steerList, tickCount);
                }
            }
            default -> {}
        }
    }
}
