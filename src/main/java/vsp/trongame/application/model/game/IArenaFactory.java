package vsp.trongame.application.model.game;

import vsp.trongame.Modus;
import vsp.trongame.applicationstub.model.rest.RESTStub;

import static vsp.trongame.Modus.LOCAL;

public class IArenaFactory {

    /**
     * Creates Instances of {@link IGame} depending on {@link Modus}.
     *
     * @param modus the modus of the game
     * @return game instance
     */
    public static IArena getArena(Modus modus, int rows, int columns) {
        if (modus == LOCAL) return new Arena(rows, columns);

        else {
            RESTStub.getInstance().createArena(rows, columns);
            return RESTStub.getInstance(); //redirects to a local Arena
        }
    }

    private IArenaFactory() {
    }

}
