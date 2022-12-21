package vsp.trongame.app.model;

import vsp.trongame.app.model.config.GameModus;
import vsp.trongame.app.model.gamemanagement.IGameManagerFactory;

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
