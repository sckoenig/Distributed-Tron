package vsp.trongame.application.model;

import vsp.trongame.application.model.gamemanagement.IGameManagerFactory;
import vsp.trongame.Modus;

public class ITronModelFactory {

    /**
     * Creates Instances of {@link ITronModel} depending on {@link Modus}.
     * @param modus the modus of the game.
     * @return model instance.
     */
    public static ITronModel getTronModel(Modus modus){
        return switch (modus){
            case LOCAL -> (ITronModel) IGameManagerFactory.getGameManager(Modus.LOCAL);
            case NETWORK -> null;
        };
    }

    private ITronModelFactory(){}

}
