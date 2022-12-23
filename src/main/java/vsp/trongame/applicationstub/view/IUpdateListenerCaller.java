package vsp.trongame.applicationstub.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.applicationstub.Service;

import java.util.List;
import java.util.Map;

public class IUpdateListenerCaller implements ITronModel.IUpdateListener {

    IUpdateListenerCallee callee = new IUpdateListenerCallee();
    @Override
    public void updateOnRegistration(int id) {

    }

    @Override
    public void updateOnKeyMappings(Map<String, String> mappings) {
        for (Map.Entry<String, String> key : mappings.entrySet()){
            //TODO
        }


        callee.call(Service.UPDATE_KEYMAPPING.toString(),);
    }

    @Override
    public void updateOnArena(int rows, int columns) {

    }

    @Override
    public void updateOnState(String state) {

    }

    @Override
    public void updateOnGameStart() {

    }

    @Override
    public void updateOnGameResult(String color, String result) {

    }

    @Override
    public void updateOnCountDown(int value) {

    }

    @Override
    public void updateOnField(Map<String, List<Coordinate>> field) {

    }

}
