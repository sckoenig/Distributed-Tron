package vsp.trongame.applicationstub.model.gamemanagement;

import vsp.trongame.application.model.datatypes.GameState;
import vsp.trongame.application.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.util.RemoteId;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;
import vsp.trongame.middleware.Middleware;

import java.util.ArrayList;
import java.util.List;

import static vsp.trongame.applicationstub.util.Service.*;

public class GameManagerCallee implements IRemoteObject {

    private final IGameManager gameManager;

    public GameManagerCallee(IGameManager gameManager) {
        this.gameManager = gameManager;
        // can be called from remote
        IRegister middleware = Middleware.getInstance();
        middleware.registerRemoteObject(HANDLE_GAME_STATE.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(HANDLE_MANAGED_PLAYERS.ordinal(), RemoteId.STRING_ID, this);
    }

    @Override
    public void call(int serviceID, int[] parameters, String... stringParameters) {
        Service service = Service.getByOrdinal(serviceID);
        switch (service){
            case HANDLE_GAME_STATE -> {
                if(parameters.length == 1){
                    gameManager.handleGameState(GameState.getByOrdinal(parameters[0]));
                }
            }
            case HANDLE_MANAGED_PLAYERS -> {
                if(parameters.length <= 7 && parameters.length > 0){
                    int id = parameters[0];
                    List<Integer> playerIds = new ArrayList<>();
                    for(int i = 1; i < parameters.length; i++){
                        playerIds.add(parameters[i]);
                    }
                    gameManager.handleManagedPlayers(id, playerIds);
                }
            }
            default -> {}
        }
    }
}
