package vsp.trongame.applicationstub.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.TronColor;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.Service;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;

import java.util.HashMap;
import java.util.Map;

public class IGameManagerCallee implements IRemoteObject {

    private IGameManager gameManager;
    private IRegister middleware;

    public void setGameManager(IGameManager gameManager){
        this.gameManager = gameManager;
    }

    @Override
    public void call(int serviceID, int... parameters) {
        Service service = Service.getByOrdinal(serviceID);
        switch (service){
            case HANDLE_GAME_STATE -> {
                if(parameters.length == 1){
                    gameManager.handleGameState(GameState.getByOrdinal(parameters[0]));
                }
            }
            //1. Parameter ist die id; 2.Parameter ist die Map der Farben mit den Spielern
            case HANDLE_MANAGED_PLAYERS -> {
                int straight = parameters.length % 2;
                if(parameters.length < 13 && parameters.length > 0 && straight > 0){
                    int id = parameters[0];
                    Map<Integer, TronColor> playerMap = new HashMap<>();
                    for(int i = 1; i < parameters.length; i+=2){
                        playerMap.put(parameters[i], TronColor.getByOrdinal(parameters[i+1]));
                    }
                    gameManager.handleManagedPlayers(id, playerMap);
                }
            }
            case HANDLE_GAME_TICK -> {
                if(parameters.length==1){
                    gameManager.handleGameTick(parameters[0]);
                }
            }
            default -> {}
        }
    }
}
