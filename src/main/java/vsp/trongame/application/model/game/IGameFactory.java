package vsp.trongame.application.model.game;


import vsp.trongame.Modus;
import vsp.trongame.applicationstub.model.game.GameCaller;
import vsp.trongame.applicationstub.model.rest.RESTStub;

public class IGameFactory {

    /**
     * Creates Instances of {@link IGame} depending on {@link Modus}.
     * @param modus the modus of the game
     * @return game instance
     */
    public static IGame getGame(Modus modus){
        return switch (modus){
            case LOCAL -> new Game();
            case NETWORK -> new GameCaller();
            case REST -> RESTStub.getInstance();
        };
    }

    private IGameFactory(){}

}
