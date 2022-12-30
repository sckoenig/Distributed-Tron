package vsp.trongame.applicationstub.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.GameResult;
import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.TronColor;
import vsp.trongame.applicationstub.Service;
import vsp.trongame.middleware.IRemoteInvocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class IUpdateListenerCaller implements ITronModel.IUpdateListener {

    IRemoteInvocation middleware;
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
        middleware.invoke("", Service.UPDATE_ARENA.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE, parameters);
    }

    @Override
    public void updateOnState(String state) {
        int[] parameters = new int[1];
        parameters[0] = GameState.valueOf(state).ordinal();
        middleware.invoke("", Service.UPDATE_ARENA.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE, parameters);
    }

    @Override
    public void updateOnGameStart() {
        int[] parameters = new int[0];
        middleware.invoke("", Service.UPDATE_START.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE, parameters);
    }

    @Override
    public void updateOnGameResult(String color, String result) {
        int[] parameters = new int[2];
        parameters[0] = TronColor.valueOf(color).ordinal();
        parameters[1] = GameResult.valueOf(result).ordinal();
        middleware.invoke("", Service.UPDATE_ARENA.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE, parameters);
    }

    @Override
    public void updateOnCountDown(int value) {
        int[] parameters = new int[1];
        parameters[0] = value;
        middleware.invoke("", Service.UPDATE_ARENA.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE, parameters);
    }

    @Override
    public void updateOnField(Map<String, List<Coordinate>> field) {
        List<Integer> parametersList = new ArrayList<>();
        for (Map.Entry<String, List<Coordinate>> entry : field.entrySet()) {
            List<Coordinate> firstFour = entry.getValue().stream().limit(4).toList();
            if(firstFour.size() >= 4){
                parametersList.add(TronColor.valueOf(entry.getKey()).ordinal());
                firstFour.forEach(coordinate -> {
                    parametersList.add(coordinate.x);
                    parametersList.add(coordinate.y);
                });
            }
        }
        int[] parameters = new int[parametersList.size()];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = parametersList.get(i);
        }
        middleware.invoke("", Service.UPDATE_FIELD.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE, parameters);
    }
}
