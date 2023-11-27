module com.example.itccfinals {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;


    opens com.example.itccfinals to javafx.fxml;
    exports com.example.itccfinals;
}