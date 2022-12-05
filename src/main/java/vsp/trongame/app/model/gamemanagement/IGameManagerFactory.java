package vsp.trongame.app.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameModus;

public class IGameManagerFactory {

    /**
     * Creates Instances of {@link IGameManager} depending on {@link GameModus}.
     * @param modus the modus of the game.
     * @return game manager instance.
     */
    public static IGameManager getGameManager(GameModus modus){
        return switch (modus){
            case LOCAL -> new GameManager(modus);
            case NETWORK -> null;
        };
    }

    private IGameManagerFactory(){}

}
