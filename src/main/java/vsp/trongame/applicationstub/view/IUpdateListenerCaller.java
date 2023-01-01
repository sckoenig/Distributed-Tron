package vsp.trongame.applicationstub.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.GameResult;
import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.TronColor;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.trongame.middleware.Middleware;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class IUpdateListenerCaller implements ITronModel.IUpdateListener, ICaller {

    private String remoteId;
    private final IRemoteInvocation middleware;
    private static final int COORDINATE_DELTA = 4;

    public IUpdateListenerCaller() {
        this.middleware = Middleware.getInstance();
    }

    @Override
    public void updateOnRegistration(int id) {
        //not needed
    }

    @Override
    public void updateOnKeyMappings(Map<String, String> mappings) {
        //not needed
    }

    @Override
    public void updateOnArena(int rows, int columns) {
        int[] parameters = new int[2];
        parameters[0] = rows;
        parameters[1] = columns;
        middleware.invoke(remoteId, Service.UPDATE_ARENA.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, parameters);
    }

    @Override
    public void updateOnState(String state) {
        int[] parameters = new int[1];
        parameters[0] = GameState.valueOf(state).ordinal();
        middleware.invoke(remoteId, Service.UPDATE_STATE.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, parameters);
    }

    @Override
    public void updateOnGameStart() {
        int[] parameters = new int[0];
        middleware.invoke(remoteId, Service.UPDATE_START.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, parameters);
    }

    @Override
    public void updateOnGameResult(String color, String result) {
        int[] parameters = new int[2];
        parameters[0] = TronColor.getTronColorByHex(color).ordinal();
        parameters[1] = GameResult.getGameResultByText(result).ordinal();
        middleware.invoke(remoteId, Service.UPDATE_RESULT.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, parameters);
    }

    @Override
    public void updateOnCountDown(int value) {
        int[] parameters = new int[1];
        parameters[0] = value;
        middleware.invoke(remoteId, Service.UPDATE_COUNTDOWN.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, parameters);
    }

    @Override
    public void updateOnField(Map<String, List<Coordinate>> field) {
        List<Integer> parametersList = new ArrayList<>();
        parametersList.add(field.size()); //playercount
        for (Map.Entry<String, List<Coordinate>> entry : field.entrySet()) {
            int skipValue = 0;
            if (entry.getValue().size() > COORDINATE_DELTA) {
                skipValue = entry.getValue().size() - COORDINATE_DELTA;
            }

            List<Coordinate> lastFour = entry.getValue().stream().skip(skipValue).toList();
            parametersList.add(TronColor.getTronColorByHex(entry.getKey()).ordinal());
            lastFour.forEach(coordinate -> {
                parametersList.add(coordinate.x);
                parametersList.add(coordinate.y);
            });
        }
        int[] parameters = new int[parametersList.size()];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = parametersList.get(i);
        }
        middleware.invoke(remoteId, Service.UPDATE_FIELD.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE, parameters);
    }

    @Override
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }
}
