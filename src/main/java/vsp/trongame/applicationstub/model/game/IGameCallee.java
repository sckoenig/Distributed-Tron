package vsp.trongame.applicationstub.model.game;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.DirectionChange;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.app.model.gamemanagement.IGameManagerFactory;
import vsp.trongame.applicationstub.ICaller;
import vsp.trongame.applicationstub.Service;
import vsp.trongame.applicationstub.model.gamemanagement.IGameManagerCaller;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;

import java.util.ArrayList;
import java.util.List;

public class IGameCallee implements IRemoteObject {

    private IGame game;
    private IRegister middleware;

    public void setGame(IGame game){
        this.game = game;
    }

    @Override
    public void call(int serviceID, int... parameters) {
        Service service = Service.getByOrdinal(serviceID);
        switch (service){
            case PREPARE -> {
                if (parameters.length == 1){
                    int playerCount = parameters[0];
                    game.prepare(playerCount);
                }
            }
            case REGISTER -> {
                ICaller caller = new IGameManagerCaller();
                caller.setRemoteId("TEST");
                IGameManager gm = (IGameManager) caller;
                game.register(gm, );
            }
            case HANDLE_STEERS -> {
                if(parameters.length < 13 && parameters.length > 0){
                    int tickCount = parameters[0];
                    List<Steer> steerList = new ArrayList<>();
                    for(int i = 1; i < parameters.length; i+=2){
                        Steer steer = new Steer(parameters[i], DirectionChange.getByOrdinal(parameters[i+1]));
                        steerList.add(steer);
                    }
                    game.handleSteers(steerList, tickCount);
                }
            }
            default -> {}
        }

    }
}
