module pos.javafxpos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens pos.javafxpos to javafx.fxml;
    exports models;
    exports posMenu;
}