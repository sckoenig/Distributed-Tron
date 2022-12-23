package vsp.trongame.applicationstub.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.*;
import vsp.trongame.applicationstub.Service;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IUpdateListenerCallee implements IRemoteObject {

    private ITronModel.IUpdateListener updateListener;
    private IRegister middleware;

    @Override
    public void call(int serviceID, int... parameters) {
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
                if(parameters.length == 1) {
                    updateListener.updateOnState(GameState.getByOrdinal(parameters[0]).name());
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
        }
    }
}
