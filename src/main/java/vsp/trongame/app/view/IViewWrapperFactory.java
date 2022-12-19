package vsp.trongame.app.view;

import vsp.trongame.app.model.datatypes.GameModus;

import java.io.IOException;

public class IViewWrapperFactory {

    public static IViewWrapper getViewWrapper(GameModus modus) throws IOException {
        return switch (modus){
            case LOCAL -> new ViewWrapper();
            case NETWORK -> null;
        };
    }

    private IViewWrapperFactory(){}

}
