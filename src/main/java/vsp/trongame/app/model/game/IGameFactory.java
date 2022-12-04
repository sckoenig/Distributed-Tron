package vsp.trongame.app.model.game;

import vsp.trongame.app.model.datatypes.GameModus;

public class IGameFactory {

    public static IGame getGame(GameModus modus){
        return switch (modus){
            case LOCAL -> new Game();
            case NETWORK -> null;
        };
    }


}
