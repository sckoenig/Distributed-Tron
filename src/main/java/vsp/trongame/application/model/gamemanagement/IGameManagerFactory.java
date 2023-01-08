package vsp.trongame.application.model.gamemanagement;

import vsp.trongame.Modus;
import vsp.trongame.applicationstub.model.gamemanagement.GameManagerCaller;

public class IGameManagerFactory {

    /**
     * Creates Instances of {@link IGameManager} depending on {@link Modus}.
     * @param modus the modus of the game.
     * @return game manager instance.
     */
    public static IGameManager getGameManager(Modus modus){
        return switch (modus){
            case LOCAL -> new GameManager();
            case RPC, REST -> new GameManagerCaller();
        };
    }

    private IGameManagerFactory(){}

}
