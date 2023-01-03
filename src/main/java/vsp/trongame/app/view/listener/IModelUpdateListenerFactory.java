package vsp.trongame.app.view.listener;

import vsp.trongame.app.model.IModelUpdateListener;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.applicationstub.view.IModelUpdateListenerCaller;

public class IModelUpdateListenerFactory {

    /**
     * Creates Instances of {@link IModelUpdateListener} depending on {@link GameModus}.
     *
     * @param modus the modus of the game.
     * @return view wrapper instance.
     */
    public static IModelUpdateListener getUpdateListener(GameModus modus){
        return switch (modus) {
            case LOCAL -> new TronModelUpdateListener();
            case NETWORK -> new IModelUpdateListenerCaller();
        };
    }

    private IModelUpdateListenerFactory() {
    }


}
