package vsp.trongame.app.model.game;

import vsp.trongame.app.model.datatypes.GameModus;

import java.util.concurrent.ExecutorService;

public class IGameFactory {

    /**
     * Creates Instances of {@link IGame} depending on {@link GameModus}.
     * @param modus the modus of the game
     * @return game instance
     */
    public static IGame getGame(GameModus modus, ExecutorService executorService, int waitingTimer, int rows, int columns, int speed){
        return switch (modus){
            case LOCAL -> new Game(executorService, waitingTimer, rows, columns, speed);
            case NETWORK -> null;
        };
    }

    private IGameFactory(){}

}
