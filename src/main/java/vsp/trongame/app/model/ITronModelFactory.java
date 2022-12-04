package vsp.trongame.app.model;

import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanagement.IGameManagerFactory;

public class ITronModelFactory {

    public static ITronModel getTronModel(GameModus modus){
        return switch (modus){
            case LOCAL -> (ITronModel) IGameManagerFactory.getGameManager(GameModus.LOCAL);
            case NETWORK -> null;
        };
    }

}
