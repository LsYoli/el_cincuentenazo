module com.example.el_cincuentenazo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.el_cincuentenazo to javafx.fxml;
    exports com.example.el_cincuentenazo;
    exports com.example.el_cincuentenazo.modelo;
    opens com.example.el_cincuentenazo.modelo to javafx.fxml;
}