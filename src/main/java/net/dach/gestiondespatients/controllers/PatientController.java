package net.dach.gestiondespatients.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.dach.gestiondespatients.enitities.Patient;
import net.dach.gestiondespatients.service.CabinetService;
import java.util.List;
import java.util.Optional;

public class PatientController {
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, Integer> idColumn;
    @FXML
    private TableColumn<Patient, String> nomColumn;
    @FXML
    private TableColumn<Patient, String> prenomColumn;
    @FXML
    private TableColumn<Patient, String> telColumn;
    @FXML
    private TableColumn<Patient, String> adresseColumn;
    @FXML
    private TextField nomField, prenomField, telField, adresseField, searchField;
    @FXML
    private DatePicker datePicker;

    private final CabinetService service = new CabinetService();
    private Patient selectedPatient;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadPatients();
        setupTableSelection();
        setupDatePicker();
    }

    private void setupDatePicker() {
        // Set the date format to dd/mm/yyyy
        datePicker.setPromptText("dd/mm/yyyy");

        // Set a custom converter to format the date
        javafx.util.StringConverter<java.time.LocalDate> dateConverter = new javafx.util.StringConverter<java.time.LocalDate>() {
            private final java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(java.time.LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public java.time.LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return java.time.LocalDate.parse(string, dateFormatter);
                    } catch (Exception e) {
                        showError("Erreur de format", "Format de date invalide", "Veuillez entrer la date au format dd/mm/yyyy.");
                        return null;
                    }
                } else {
                    return null;
                }
            }
        };

        datePicker.setConverter(dateConverter);

        // Add a listener to commit the text when the user finishes typing
        datePicker.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // When focus is lost
                String text = datePicker.getEditor().getText();
                if (text != null && !text.isEmpty()) {
                    java.time.LocalDate date = dateConverter.fromString(text);
                    datePicker.setValue(date);
                }
            }
        });

        // Also commit when Enter key is pressed
        datePicker.getEditor().setOnAction(event -> {
            String text = datePicker.getEditor().getText();
            if (text != null && !text.isEmpty()) {
                java.time.LocalDate date = dateConverter.fromString(text);
                datePicker.setValue(date);
            }
        });
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
    }

    private void setupTableSelection() {
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPatient = newSelection;
                nomField.setText(selectedPatient.getNom());
                prenomField.setText(selectedPatient.getPrenom());
                datePicker.setValue(new java.sql.Date(selectedPatient.getDateNaissance().getTime()).toLocalDate());
                telField.setText(selectedPatient.getTelephone());
                adresseField.setText(selectedPatient.getAdresse());
            }
        });
    }

    private void loadPatients() {
        try {
            List<Patient> patients = service.getAllPatients();
            ObservableList<Patient> observableList = FXCollections.observableArrayList(patients);
            patientTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement", "Impossible de charger les patients", e.getMessage());
        }
    }

    @FXML
    private void addPatient() {
        try {
            // Check if required fields are filled
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty()) {
                showError("Erreur de saisie", "Champs obligatoires", "Les champs Nom et Prénom sont obligatoires.");
                return;
            }

            // Check if date is selected
            if (datePicker.getValue() == null) {
                showError("Erreur de saisie", "Date manquante", "Date manquante : veuillez sélectionner une date de naissance.");
                return;
            }

            Patient patient = new Patient(
                    0, // ID will be auto-generated by the database
                    nomField.getText(),
                    prenomField.getText(),
                    java.sql.Date.valueOf(datePicker.getValue()),
                    telField.getText(),
                    adresseField.getText()
            );
            service.addPatient(patient);

            clearForm();
            loadPatients();
            showInfo("Succès", "Patient ajouté", "Le patient a été ajouté avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Une erreur est survenue", "Impossible d'ajouter le patient: " + e.getMessage());
        }
    }

    @FXML
    private void updatePatient() {
        if (selectedPatient == null) {
            showError("Erreur", "Aucun patient sélectionné", "Veuillez sélectionner un patient à modifier.");
            return;
        }

        try {
            // Check if required fields are filled
            if (nomField.getText().isEmpty() || prenomField.getText().isEmpty()) {
                showError("Erreur de saisie", "Champs obligatoires", "Les champs Nom et Prénom sont obligatoires.");
                return;
            }

            // Check if date is selected
            if (datePicker.getValue() == null) {
                showError("Erreur de saisie", "Date manquante", "Date manquante : veuillez sélectionner une date de naissance.");
                return;
            }

            Patient updatedPatient = new Patient(
                    selectedPatient.getId(),
                    nomField.getText(),
                    prenomField.getText(),
                    java.sql.Date.valueOf(datePicker.getValue()),
                    telField.getText(),
                    adresseField.getText()
            );
            service.updatePatient(updatedPatient);

            clearForm();
            loadPatients();
            selectedPatient = null;
            showInfo("Succès", "Patient modifié", "Le patient a été modifié avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Une erreur est survenue", "Impossible de modifier le patient: " + e.getMessage());
        }
    }

    @FXML
    private void deletePatient() {
        if (selectedPatient == null) {
            showError("Erreur", "Aucun patient sélectionné", "Veuillez sélectionner un patient à supprimer.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer le patient");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce patient ? Cette action est irréversible.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.deletePatient(selectedPatient.getId());
                clearForm();
                loadPatients();
                selectedPatient = null;
                showInfo("Succès", "Patient supprimé", "Le patient a été supprimé avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erreur", "Une erreur est survenue", "Impossible de supprimer le patient: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        datePicker.setValue(null);
        telField.clear();
        adresseField.clear();
        selectedPatient = null;
        patientTable.getSelectionModel().clearSelection();
    }

    private void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void searchPatient() {
        try {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                showError("Erreur de recherche", "Terme de recherche vide", "Veuillez entrer un terme de recherche.");
                return;
            }

            List<Patient> patients = service.searchPatientsByNom(searchTerm);
            if (patients.isEmpty()) {
                showInfo("Résultat de recherche", "Aucun résultat", "Aucun patient trouvé avec ce terme de recherche.");
                return;
            }

            ObservableList<Patient> observableList = FXCollections.observableArrayList(patients);
            patientTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de recherche", "Impossible d'effectuer la recherche", e.getMessage());
        }
    }

    @FXML
    private void resetSearch() {
        searchField.clear();
        loadPatients();
    }
}
