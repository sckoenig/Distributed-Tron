package vsp.trongame.app.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameModus;

public class IGameDataFactory {

    public static IGameData getGameData(GameModus modus){
        return switch (modus){
            case LOCAL -> new ObservableGameData();
            case NETWORK -> null;
        };
    }

}
