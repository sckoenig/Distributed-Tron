package vsp.trongame.app.view.overlays;

import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanager.Config;
import vsp.trongame.app.model.gamemanager.GameManager;
import vsp.trongame.app.model.gamemanager.IGameManager;

import java.io.IOException;

public class ITronViewFactory {

    public static ITronView getTronView(GameModus modus, String config) throws IOException {
        return switch (modus){
            case LOCAL -> new TronView(config);
            case NETWORK -> null;
        };
    }

}
