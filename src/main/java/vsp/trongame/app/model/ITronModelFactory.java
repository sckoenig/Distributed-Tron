package vsp.trongame.app.model;

import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanager.IGameManagerFactory;
import vsp.trongame.app.model.gamemanager.ITronModel;

public class ITronModelFactory {

    public static ITronModel getTronModel(GameModus modus){
        return switch (modus){
            case LOCAL -> (ITronModel) IGameManagerFactory.getGameManager(GameModus.LOCAL);
            case NETWORK -> null;
        };
    }

}
