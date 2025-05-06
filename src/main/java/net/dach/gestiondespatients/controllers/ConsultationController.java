package net.dach.gestiondespatients.controllers;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import net.dach.gestiondespatients.dao.ConsultationDao;
import net.dach.gestiondespatients.dao.IConsultationDao;
import net.dach.gestiondespatients.dao.PatientDao;
import net.dach.gestiondespatients.enitities.Consultation;
import net.dach.gestiondespatients.enitities.Patient;
import net.dach.gestiondespatients.enitities.StatutConsultation;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ConsultationController {
    @FXML
    private TableView<Consultation> consultationTable;
    @FXML
    private TableColumn<Consultation, Integer> idColumn;
    @FXML
    private TableColumn<Consultation, String> patientColumn;
    @FXML
    private TableColumn<Consultation, String> dateColumn;
    @FXML
    private TableColumn<Consultation, String> motifColumn;
    @FXML
    private TableColumn<Consultation, String> statutColumn;

    @FXML
    private ComboBox<Patient> patientCombo;
    @FXML
    private TextField motifField, searchField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> statutCombo;

    private final IConsultationDao consultationDao = new ConsultationDao();
    private final PatientDao patientDao = new PatientDao();
    private Consultation selectedConsultation;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadPatients();
        loadConsultations();
        setupTableSelection();
        statutCombo.getItems().addAll(StatutConsultation.getAllStatuts());
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(cellData -> {
            try {
                Patient patient = patientDao.findById(cellData.getValue().getPatientId());
                return javafx.beans.binding.Bindings.createStringBinding(
                    () -> patient != null ? patient.getNom() + " " + patient.getPrenom() : "Inconnu"
                );
            } catch (Exception e) {
                e.printStackTrace();
                return javafx.beans.binding.Bindings.createStringBinding(() -> "Erreur");
            }
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        motifColumn.setCellValueFactory(new PropertyValueFactory<>("motif"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    private void setupTableSelection() {
        consultationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedConsultation = newSelection;
                try {
                    Patient patient = patientDao.findById(selectedConsultation.getPatientId());
                    patientCombo.getSelectionModel().select(patient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                datePicker.setValue(new java.sql.Date(selectedConsultation.getDateConsultation().getTime()).toLocalDate());
                motifField.setText(selectedConsultation.getMotif());
                statutCombo.getSelectionModel().select(selectedConsultation.getStatut());
            }
        });
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientDao.findAll();
            ObservableList<Patient> observableList = FXCollections.observableArrayList(patients);
            patientCombo.setItems(observableList);

            // Set up the display of patients in the combo box
            patientCombo.setConverter(new StringConverter<Patient>() {
                @Override
                public String toString(Patient patient) {
                    return patient == null ? "" : patient.getNom() + " " + patient.getPrenom() + " - " + patient.getTelephone();
                }

                @Override
                public Patient fromString(String string) {
                    return null; // Not needed for this use case
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement", "Impossible de charger les patients", e.getMessage());
        }
    }

    private void loadConsultations() {
        try {
            List<Consultation> consultations = consultationDao.findAll();
            ObservableList<Consultation> observableList = FXCollections.observableArrayList(consultations);
            consultationTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement", "Impossible de charger les consultations", e.getMessage());
        }
    }

    @FXML
    private void addConsultation() {
        try {
            // Check if patient is selected
            if (patientCombo.getSelectionModel().getSelectedItem() == null) {
                showError("Erreur de saisie", "Patient manquant", "Veuillez sélectionner un patient.");
                return;
            }

            // Check if date is selected
            if (datePicker.getValue() == null) {
                showError("Erreur de saisie", "Date manquante", "Veuillez sélectionner une date pour la consultation.");
                return;
            }

            // Check if status is selected
            if (statutCombo.getSelectionModel().getSelectedItem() == null) {
                showError("Erreur de saisie", "Statut manquant", "Veuillez sélectionner un statut pour la consultation.");
                return;
            }

            Consultation consultation = new Consultation(
                    0, // ID will be auto-generated by the database
                    patientCombo.getSelectionModel().getSelectedItem().getId(),
                    java.sql.Date.valueOf(datePicker.getValue()),
                    motifField.getText(),
                    statutCombo.getSelectionModel().getSelectedItem()
            );
            consultationDao.save(consultation);
            clearForm();
            loadConsultations();
            showInfo("Succès", "Consultation ajoutée", "La consultation a été ajoutée avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Une erreur est survenue", "Impossible d'ajouter la consultation: " + e.getMessage());
        }
    }

    @FXML
    private void updateConsultation() {
        if (selectedConsultation == null) {
            showError("Erreur", "Aucune consultation sélectionnée", "Veuillez sélectionner une consultation à modifier.");
            return;
        }

        try {
            // Check if patient is selected
            if (patientCombo.getSelectionModel().getSelectedItem() == null) {
                showError("Erreur de saisie", "Patient manquant", "Veuillez sélectionner un patient.");
                return;
            }

            // Check if date is selected
            if (datePicker.getValue() == null) {
                showError("Erreur de saisie", "Date manquante", "Veuillez sélectionner une date pour la consultation.");
                return;
            }

            // Check if status is selected
            if (statutCombo.getSelectionModel().getSelectedItem() == null) {
                showError("Erreur de saisie", "Statut manquant", "Veuillez sélectionner un statut pour la consultation.");
                return;
            }

            Consultation updatedConsultation = new Consultation(
                    selectedConsultation.getId(),
                    patientCombo.getSelectionModel().getSelectedItem().getId(),
                    java.sql.Date.valueOf(datePicker.getValue()),
                    motifField.getText(),
                    statutCombo.getSelectionModel().getSelectedItem()
            );
            consultationDao.update(updatedConsultation);
            clearForm();
            loadConsultations();
            selectedConsultation = null;
            showInfo("Succès", "Consultation modifiée", "La consultation a été modifiée avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Une erreur est survenue", "Impossible de modifier la consultation: " + e.getMessage());
        }
    }

    @FXML
    private void deleteConsultation() {
        if (selectedConsultation == null) {
            showError("Erreur", "Aucune consultation sélectionnée", "Veuillez sélectionner une consultation à supprimer.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer la consultation");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette consultation ? Cette action est irréversible.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                consultationDao.delete(selectedConsultation.getId());
                clearForm();
                loadConsultations();
                selectedConsultation = null;
                showInfo("Succès", "Consultation supprimée", "La consultation a été supprimée avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
                showError("Erreur", "Une erreur est survenue", "Impossible de supprimer la consultation: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clearForm() {
        patientCombo.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        motifField.clear();
        statutCombo.getSelectionModel().clearSelection();
        selectedConsultation = null;
        consultationTable.getSelectionModel().clearSelection();
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
    private void searchConsultation() {
        try {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                showError("Erreur de recherche", "Terme de recherche vide", "Veuillez entrer un ID de patient.");
                return;
            }

            try {
                int patientId = Integer.parseInt(searchTerm);

                // First, check if the patient exists
                Patient patient = patientDao.findById(patientId);
                if (patient == null) {
                    showError("Erreur de recherche", "Patient introuvable", "Aucun patient trouvé avec cet ID.");
                    return;
                }

                // If patient exists, select it in the combo box
                patientCombo.getSelectionModel().select(patient);

                // Then search for consultations
                List<Consultation> consultations = consultationDao.findByPatientId(patientId);
                if (consultations.isEmpty()) {
                    showInfo("Résultat de recherche", "Aucun résultat", 
                        "Aucune consultation trouvée pour le patient: " + 
                        patient.getNom() + " " + patient.getPrenom() + " (ID: " + patient.getId() + ")");
                    return;
                }

                ObservableList<Consultation> observableList = FXCollections.observableArrayList(consultations);
                consultationTable.setItems(observableList);

                showInfo("Résultat de recherche", "Consultations trouvées", 
                    consultations.size() + " consultation(s) trouvée(s) pour le patient: " + 
                    patient.getNom() + " " + patient.getPrenom() + " (ID: " + patient.getId() + ")");
            } catch (NumberFormatException e) {
                showError("Erreur de recherche", "ID invalide", "Veuillez entrer un ID de patient valide (nombre entier).");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de recherche", "Impossible d'effectuer la recherche", e.getMessage());
        }
    }

    @FXML
    private void resetSearch() {
        searchField.clear();
        loadConsultations();
    }
}
