package vsp.trongame.applicationstub.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.*;
import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.TronColor;
import vsp.trongame.applicationstub.util.RemoteId;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;
import vsp.trongame.middleware.Middleware;

import java.util.*;

import static vsp.trongame.applicationstub.util.Service.*;

import java.util.*;

public class IUpdateListenerCallee implements IRemoteObject {

    private ITronModel.IUpdateListener updateListener;

    public IUpdateListenerCallee() {

        // can be called from remote
        IRegister middleware = Middleware.getInstance();
        middleware.registerRemoteObject(UPDATE_ARENA.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(UPDATE_START.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(UPDATE_RESULT.ordinal(),RemoteId.STRING_ID,  this);
        middleware.registerRemoteObject(UPDATE_COUNTDOWN.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(UPDATE_FIELD.ordinal(), RemoteId.STRING_ID, this);
    }

    public void setUpdateListener(ITronModel.IUpdateListener updateListener){
        this.updateListener = updateListener;
    }

    @Override
    public void call(int serviceID, int[] parameters,  String... stringParameters) {
        Service service = Service.getByOrdinal(serviceID);
        switch (service){
            case UPDATE_ARENA -> {
                if(parameters.length == 2){
                    int rows = parameters[0];
                    int columns = parameters[1];
                    updateListener.updateOnArena(rows,columns);
                }
            }
            case UPDATE_STATE -> {
                if(parameters.length > 0) {
                    int state = parameters[0];
                    updateListener.updateOnState(GameState.getByOrdinal(state).name());
                }
            }
            case UPDATE_START -> {
                updateListener.updateOnGameStart();
            }
            case UPDATE_RESULT -> {
                //TODO
                if(parameters.length >= 2){
                    String color = TronColor.getByOrdinal(parameters[0]).name();
                    StringBuilder result = new StringBuilder();
                    for(int i = 1 ; i < parameters.length; i+=9){
                        result.append((char) parameters[i]);
                    }
                    updateListener.updateOnGameResult(color, result.toString());
                }
            }
            case UPDATE_COUNTDOWN -> {
                if(parameters.length == 1){
                    updateListener.updateOnCountDown(parameters[0]);
                }
            }
            case UPDATE_FIELD -> {
                int playerCount = parameters[0];
                int coordinatesCount = ((((parameters.length-1)-playerCount) / playerCount) /2);
                Map<String, List<Coordinate>> updateCoordinates = new HashMap<>();
                List<Coordinate> coordinates = new ArrayList<>();
                for(int i = 1 ; i < parameters.length; i+=coordinatesCount*2){
                    String color = TronColor.getByOrdinal(parameters[i]).getHex();
                    i++;
                    int k = 0;
                    for(int j = 0; j < coordinatesCount; j++){
                        coordinates.add(new Coordinate(parameters[k+i], parameters[k+i+1]));
                        k+=2;
                    }
                    updateCoordinates.put(color, coordinates);
                }
                updateListener.updateOnField(updateCoordinates);
            }
            default -> {}
        }
    }
}
