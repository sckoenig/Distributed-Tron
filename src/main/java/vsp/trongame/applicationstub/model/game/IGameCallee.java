package vsp.trongame.applicationstub.model.game;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.DirectionChange;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.app.model.gamemanagement.IGameManagerFactory;
import vsp.trongame.app.view.IUpdateListenerFactory;
import vsp.trongame.applicationstub.ICaller;
import vsp.trongame.applicationstub.Service;
import vsp.trongame.applicationstub.model.gamemanagement.IGameManagerCaller;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
                if (parameters.length == 10){
                    IGameManager managerCaller = IGameManagerFactory.getGameManager(GameModus.NETWORK);
                    String gmId = String.format("%d.%d.%d:%d", parameters[0], parameters[1], parameters[2], parameters[3]);
                    String listenerId = String.format("%d.%d.%d:%d", parameters[4], parameters[5], parameters[6], parameters[7]);
                    ((ICaller)managerCaller).setRemoteId(gmId);
                    ITronModel.IUpdateListener listenerCaller = IUpdateListenerFactory.getUpdateListener(GameModus.NETWORK);
                    ((ICaller)listenerCaller).setRemoteId(listenerId);
                    game.register(managerCaller, listenerCaller,parameters[8], parameters[9]);
                }
            }
            case HANDLE_STEERS -> {
                int straight = parameters.length % 2;
                if(parameters.length < 13 && parameters.length > 0 && straight > 0){
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
