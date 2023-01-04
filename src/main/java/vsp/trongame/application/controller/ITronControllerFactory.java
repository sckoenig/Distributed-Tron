package vsp.trongame.application.controller;

import vsp.trongame.Modus;

public class ITronControllerFactory {

    /**
     * Creates Instances of {@link ITronController} depending on {@link Modus}.
     * @param modus the modus of the game.
     * @return controller instance.
     */
    public static ITronController getTronController(Modus modus) {
        return switch (modus) {
            case LOCAL, NETWORK -> new TronController();
            // no special implementation is needed for NETWORK right now
        };
    }

    private ITronControllerFactory(){}
}
