module net.dach.gestiondespatients {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens net.dach.gestiondespatients to javafx.fxml;
    opens net.dach.gestiondespatients.controllers to javafx.fxml;
    opens net.dach.gestiondespatients.enitities to javafx.base;
    exports net.dach.gestiondespatients;
    exports net.dach.gestiondespatients.controllers;
}
