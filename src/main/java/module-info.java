module org.example.oop2_finalproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens org.example.oop2_finalproject to javafx.fxml;
    exports org.example.oop2_finalproject;
}