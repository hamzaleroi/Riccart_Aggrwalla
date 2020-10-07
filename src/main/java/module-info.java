module com.esi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;


    opens com.esi to javafx.fxml, com.fasterxml.jackson.annotation,com.fasterxml.jackson.databind;
    exports com.esi;
    exports com.esi.Entities;
}