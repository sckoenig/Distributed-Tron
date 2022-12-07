package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.ITronView;
import vsp.trongame.app.model.datatypes.GameModus;

public class IGameManagerFactory {

    /**
     * Creates Instances of {@link IGameManager} depending on {@link GameModus}.
     * @param modus the modus of the game.
     * @return game manager instance.
     */
    public static IGameManager getGameManager(GameModus modus, ITronView tronView, boolean singleView, Config config){
        return switch (modus){
            case LOCAL -> new GameManager(modus, tronView, singleView, config);
            case NETWORK -> null;
        };
    }

    private IGameManagerFactory(){}

}
