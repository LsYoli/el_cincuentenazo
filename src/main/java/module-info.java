module com.example.el_cincuentenazo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.el_cincuentenazo to javafx.fxml;
    exports com.example.el_cincuentenazo;
}