package vsp.trongame.app.controller;

import vsp.trongame.app.model.datatypes.GameModus;

public class ITronControllerFactory {

    /**
     * Creates Instances of {@link ITronController} depending on {@link GameModus}.
     * @param modus the modus of the game.
     * @return controller instance.
     */
    public static ITronController getTronController(GameModus modus) {
        return switch (modus) {
            case LOCAL, NETWORK -> new TronController();
            // no special implementation is needed for NETWORK right now
        };
    }

    private ITronControllerFactory(){}
}
