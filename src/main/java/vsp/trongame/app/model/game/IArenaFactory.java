package vsp.trongame.app.model.game;

import vsp.trongame.app.model.datatypes.GameModus;

public class IArenaFactory {

    /**
     * Creates Instances of {@link IGame} depending on {@link GameModus}.
     * @param modus the modus of the game
     * @return game instance
     */
    public static IArena getArena(GameModus modus, int rows, int columns){
        return switch (modus){
            case LOCAL -> new Arena(rows, columns);
            case NETWORK -> null; //TODO rest wrapper here
        };
    }

    private IArenaFactory(){}

}
