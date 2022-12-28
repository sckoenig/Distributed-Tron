package vsp.trongame.app.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.applicationstub.model.gamemanagement.IGameManagerCaller;

public class IGameManagerFactory {

    /**
     * Creates Instances of {@link IGameManager} depending on {@link GameModus}.
     * @param modus the modus of the game.
     * @return game manager instance.
     */
    public static IGameManager getGameManager(GameModus modus){
        return switch (modus){
            case LOCAL -> new GameManager();
            case NETWORK -> new IGameManagerCaller();
        };
    }

    private IGameManagerFactory(){}

}
