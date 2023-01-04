package vsp.trongame.app.model.game;


import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.applicationstub.model.game.GameCaller;

public class IGameFactory {

    /**
     * Creates Instances of {@link IGame} depending on {@link GameModus}.
     * @param modus the modus of the game
     * @return game instance
     */
    public static IGame getGame(GameModus modus){
        return switch (modus){
            case LOCAL -> new Game();
            case NETWORK -> new GameCaller();
        };
    }

    private IGameFactory(){}

}
