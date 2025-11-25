module com.example.practice2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires java.desktop;

    opens com.example.practice2 to javafx.fxml;
    exports com.example.practice2;
    exports com.example.practice2.testing;
    opens com.example.practice2.testing to javafx.fxml;
}