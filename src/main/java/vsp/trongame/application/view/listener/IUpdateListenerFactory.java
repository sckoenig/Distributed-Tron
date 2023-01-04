package vsp.trongame.application.view.listener;

import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.Modus;
import vsp.trongame.applicationstub.view.UpdateListenerCaller;

public class IUpdateListenerFactory {

    /**
     * Creates Instances of {@link IUpdateListener} depending on {@link Modus}.
     *
     * @param modus the modus of the game.
     * @return view wrapper instance.
     */
    public static IUpdateListener getUpdateListener(Modus modus){
        return switch (modus) {
            case LOCAL -> new UpdateListener();
            case NETWORK -> new UpdateListenerCaller();
        };
    }

    private IUpdateListenerFactory() {
    }


}
