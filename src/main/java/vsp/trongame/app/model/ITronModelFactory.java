package vsp.trongame.app.model;

import vsp.trongame.app.model.gamemanagement.IGameManagerFactory;
import vsp.trongame.app.model.util.datatypes.GameModus;

public class ITronModelFactory {

    /**
     * Creates Instances of {@link ITronModel} depending on {@link GameModus}.
     * @param modus the modus of the game.
     * @return model instance.
     */
    public static ITronModel getTronModel(GameModus modus){
        return switch (modus){
            case LOCAL -> (ITronModel) IGameManagerFactory.getGameManager(GameModus.LOCAL);
            case NETWORK -> null;
        };
    }

    private ITronModelFactory(){}

}
