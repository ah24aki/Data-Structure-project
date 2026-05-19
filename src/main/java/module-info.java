module com.example.mainfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mainfx to javafx.fxml;
    exports com.example.mainfx;
}