package vsp.trongame.app.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameModus;

public class IGameDataFactory {

    /**
     * Creates Instances of {@link IGameData} depending on {@link GameModus}.
     * @param modus the modus of the game.
     * @return game data
     */
    public static IGameData getGameData(GameModus modus){
        return switch (modus){
            case LOCAL -> new ObservableGameData();
            case NETWORK -> null;
        };
    }

    private IGameDataFactory(){}

}
