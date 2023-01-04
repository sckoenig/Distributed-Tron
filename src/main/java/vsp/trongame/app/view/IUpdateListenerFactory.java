package vsp.trongame.app.view;

import vsp.trongame.app.model.IUpdateListener;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.applicationstub.view.UpdateListenerCaller;

public class IUpdateListenerFactory {

    /**
     * Creates Instances of {@link IUpdateListener} depending on {@link GameModus}.
     *
     * @param modus the modus of the game.
     * @return view wrapper instance.
     */
    public static IUpdateListener getUpdateListener(GameModus modus){
        return switch (modus) {
            case LOCAL -> new UpdateListener();
            case NETWORK -> new UpdateListenerCaller();
        };
    }

    private IUpdateListenerFactory() {
    }


}
