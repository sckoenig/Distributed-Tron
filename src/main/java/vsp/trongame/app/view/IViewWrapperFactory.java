package vsp.trongame.app.view;


import vsp.trongame.app.model.datatypes.GameModus;

public class IViewWrapperFactory {

    /**
     * Creates Instances of {@link ITronViewWrapper} depending on {@link GameModus}.
     *
     * @param modus the modus of the game.
     * @return view wrapper instance.
     */
    public static ITronViewWrapper getViewWrapper(GameModus modus){
        return switch (modus) {
            case LOCAL, NETWORK -> new TronViewWrapper();
            // for NETWORK there is no other implementation needed at the moment
        };
    }

    private IViewWrapperFactory() {
    }

}
