package vsp.trongame.application.model.game;

import vsp.trongame.Modus;

public class IArenaFactory {

    /**
     * Creates Instances of {@link IGame} depending on {@link Modus}.
     * @param modus the modus of the game
     * @return game instance
     */
    public static IArena getArena(Modus modus, int rows, int columns){
        return switch (modus){
            case LOCAL -> new Arena(rows, columns);
            case NETWORK -> null; //TODO rest wrapper here
        };
    }

    private IArenaFactory(){}

}
