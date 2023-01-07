package vsp.trongame.application.view;


import vsp.trongame.Modus;

public class IViewWrapperFactory {

    /**
     * Creates Instances of {@link ITronViewWrapper} depending on {@link Modus}.
     *
     * @param modus the modus of the game.
     * @return view wrapper instance.
     */
    public static ITronViewWrapper getViewWrapper(Modus modus){
        return switch (modus) {
            case LOCAL, NETWORK, REST -> new TronViewWrapper();
            // for NETWORK there is no other implementation needed at the moment
        };
    }

    private IViewWrapperFactory() {
    }

}
