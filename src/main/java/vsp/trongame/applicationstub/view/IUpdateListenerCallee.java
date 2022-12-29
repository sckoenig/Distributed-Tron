package vsp.trongame.applicationstub.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.TronColor;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;
import vsp.trongame.middleware.Middleware;

import java.util.*;

import static vsp.trongame.applicationstub.util.Service.*;

public class IUpdateListenerCallee implements IRemoteObject {

    private ITronModel.IUpdateListener updateListener;

    public IUpdateListenerCallee() {

        // can be called from remote
        IRegister middleware = Middleware.getInstance();
        middleware.registerRemoteObject(UPDATE_ARENA.ordinal(), this);
        middleware.registerRemoteObject(UPDATE_START.ordinal(), this);
        middleware.registerRemoteObject(UPDATE_RESULT.ordinal(), this);
        middleware.registerRemoteObject(UPDATE_COUNTDOWN.ordinal(), this);
        middleware.registerRemoteObject(UPDATE_FIELD.ordinal(), this);
    }

    public void setUpdateListener(ITronModel.IUpdateListener updateListener){
        this.updateListener = updateListener;
    }

    @Override
    public void call(int serviceID, int[] parameters, String... stringParameters) {
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
                if(parameters.length >= 9){
                    Map<String, List<Coordinate>> updateCoordinates = new HashMap<>();
                    List<Coordinate> coordinates = new ArrayList<>();
                    for(int i = 0 ; i < parameters.length; i+=9){
                        String color = TronColor.getByOrdinal(parameters[i]).name();
                        coordinates.add(new Coordinate(i+1, i+2));
                        coordinates.add(new Coordinate(i+3, i+4));
                        coordinates.add(new Coordinate(i+5, i+6));
                        coordinates.add(new Coordinate(i+7, i+8));
                        updateCoordinates.put(color, coordinates);
                    }
                    updateListener.updateOnField(updateCoordinates);
                }
            }
            default -> {}
        }
    }
}
