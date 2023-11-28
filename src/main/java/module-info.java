module com.example.itccfinals {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
    requires java.sql;


    opens com.example.itccfinals to javafx.fxml;
    exports com.example.itccfinals;
}