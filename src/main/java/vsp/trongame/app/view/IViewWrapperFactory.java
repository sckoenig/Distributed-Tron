package vsp.trongame.app.view;


import vsp.trongame.app.model.util.datatypes.GameModus;

public class IViewWrapperFactory {

    /**
     * Creates Instances of {@link IViewWrapper} depending on {@link GameModus}.
     *
     * @param modus the modus of the game.
     * @return view wrapper instance.
     */
    public static IViewWrapper getViewWrapper(GameModus modus){
        return switch (modus) {
            case LOCAL -> new ViewWrapper();
            case NETWORK -> null; //not needed right now
        };
    }

    private IViewWrapperFactory() {
    }

}
