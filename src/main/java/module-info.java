module vsp.trongame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires view.library;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires jdk.httpserver;

    opens vsp.trongame to javafx.fxml;
    exports vsp.trongame;
    exports vsp.trongame.application.controller;
    exports vsp.trongame.application.model;
    exports vsp.trongame.application.view;
    exports vsp.trongame.application.view.overlays;
    opens vsp.trongame.application.view.overlays to javafx.fxml;
    exports vsp.trongame.application.model.gamemanagement;
    exports vsp.middleware;
    exports vsp.trongame.application.model.datatypes;
    exports vsp.middleware.clientstub;
    exports vsp.middleware.namingservice;
    opens vsp.trongame.application.view to javafx.fxml;
    exports vsp.trongame.application.view.listener;
    opens vsp.trongame.application.view.listener to javafx.fxml;
    exports vsp.trongame.applicationstub.model.rest.ressources to com.google.gson;
}