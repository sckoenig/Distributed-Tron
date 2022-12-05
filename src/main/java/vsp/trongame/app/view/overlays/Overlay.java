package vsp.trongame.app.view.overlays;

import vsp.trongame.app.model.ITronModel;

/**
 * Represents a generic overlay.
 */
public interface Overlay {

    /**
     * Initializes the overlay with the observable Model.
     * @param model observable model
     */
    void init(ITronModel.IObservableTronModel model);

}
