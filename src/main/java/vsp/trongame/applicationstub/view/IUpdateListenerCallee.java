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


public class IUpdateListenerCallee implements IRemoteObject {

    private final ITronModel.IUpdateListener updateListener;

    public IUpdateListenerCallee(ITronModel.IUpdateListener updateListener) {
        this.updateListener = updateListener;

        // can be called from remote
        IRegister middleware = Middleware.getInstance();
        middleware.registerRemoteObject(UPDATE_ARENA.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(UPDATE_START.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(UPDATE_RESULT.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(UPDATE_COUNTDOWN.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(UPDATE_FIELD.ordinal(), RemoteId.STRING_ID, this);
    }

    @Override
    public void call(int serviceID, int[] parameters, String... stringParameters) {
        Service service = Service.getByOrdinal(serviceID);
        switch (service) {
            case UPDATE_ARENA -> {
                if (parameters.length == 2) {
                    int rows = parameters[0];
                    int columns = parameters[1];
                    updateListener.updateOnArena(rows, columns);
                }
            }
            case UPDATE_STATE -> {
                if (parameters.length > 0) {
                    int state = parameters[0];
                    updateListener.updateOnState(GameState.getByOrdinal(state).name());
                }
            }
            case UPDATE_START -> {
                updateListener.updateOnGameStart();
            }
            case UPDATE_RESULT -> {
                if (parameters.length >= 2) {
                    String color = TronColor.getByOrdinal(parameters[0]).getHex();
                    String result = GameResult.getByOrdinal(parameters[1]).getResultText();
                    updateListener.updateOnGameResult(color, result);
                }
            }
            case UPDATE_COUNTDOWN -> {
                if (parameters.length == 1) {
                    updateListener.updateOnCountDown(parameters[0]);
                }
            }
            case UPDATE_FIELD -> {
                int playerCount = parameters[0];
                if (parameters.length > 1) {
                    int coordinatesCount = ((((parameters.length - 1) - playerCount) / playerCount) / 2);
                    Map<String, List<Coordinate>> updateCoordinates = new HashMap<>();
                    for (int i = 1; i < parameters.length; i += coordinatesCount * 2 + 1) {
                        List<Coordinate> coordinates = new ArrayList<>();
                        String color = TronColor.getByOrdinal(parameters[i]).getHex();
                        for (int j = 1; j < coordinatesCount; j+=2) {
                            coordinates.add(new Coordinate(parameters[i+j], parameters[i+j+1]));
                        }
                        updateCoordinates.put(color, coordinates);
                    }
                    updateListener.updateOnField(updateCoordinates);
                }
            }
            default -> {
            }
        }
    }
}
