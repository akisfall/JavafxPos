module pos.javafxpos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens pos.javafxpos to javafx.fxml;
    exports models;
    exports posMenu;
}