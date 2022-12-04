package vsp.trongame.app.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameModus;

public class IGameManagerFactory {

    public static IGameManager getGameManager(GameModus modus){
        return switch (modus){
            case LOCAL -> new GameManager(modus);
            case NETWORK -> null;
        };
    }

}
