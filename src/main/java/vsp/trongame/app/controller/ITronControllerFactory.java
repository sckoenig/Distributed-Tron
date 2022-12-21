package vsp.trongame.app.controller;

import vsp.trongame.app.model.config.GameModus;

public class ITronControllerFactory {

    public static ITronController getTronController(GameModus modus) {
        return switch (modus) {
            case LOCAL -> new TronController();
            case NETWORK -> null;
        };
    }

    private ITronControllerFactory(){}
}
