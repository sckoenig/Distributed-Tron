package vsp.trongame.app.view;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.util.datatypes.GameModus;
import vsp.trongame.applicationstub.view.IUpdateListenerCaller;

public class IUpdateListenerFactory {

    /**
     * Creates Instances of {@link ITronModel.IUpdateListener} depending on {@link GameModus}.
     *
     * @param modus the modus of the game.
     * @return view wrapper instance.
     */
    public static ITronModel.IUpdateListener getUpdateListener(GameModus modus){
        return switch (modus) {
            case LOCAL -> (ITronModel.IUpdateListener) IViewWrapperFactory.getViewWrapper(GameModus.LOCAL);
            case NETWORK -> new IUpdateListenerCaller();
        };
    }

    private IUpdateListenerFactory() {
    }


}
