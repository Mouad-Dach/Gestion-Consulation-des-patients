package net.dach.gestiondespatients.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private BorderPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load patients view by default
        handlePatients();
    }

    @FXML
    private void handlePatients() {
        loadView("/net/dach/gestiondespatients/views/patients-view.fxml");
    }

    @FXML
    private void handleConsultations() {
        loadView("/net/dach/gestiondespatients/views/consultation-view.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            mainContainer.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
