module vsp.trongame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires view.library;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens vsp.trongame to javafx.fxml;
    exports vsp.trongame;
    exports vsp.trongame.app.controller;
    exports vsp.trongame.app.model;
    exports vsp.trongame.app.model.config;
    exports vsp.trongame.app.view;
    exports vsp.trongame.app.view.overlays;
    opens vsp.trongame.app.view.overlays to javafx.fxml;
}