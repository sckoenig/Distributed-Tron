module vsp.trongame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires view.library;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens vsp.trongame to javafx.fxml;
    exports vsp.trongame;
}