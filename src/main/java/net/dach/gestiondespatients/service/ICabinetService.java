package net.dach.gestiondespatients.service;



import net.dach.gestiondespatients.enitities.Patient;
import java.util.List;

public interface ICabinetService {
    List<Patient> getAllPatients() throws Exception;
    void addPatient(Patient patient) throws Exception;
    void updatePatient(Patient patient) throws Exception;
    void deletePatient(int id) throws Exception;
    List<Patient> searchPatientsByNom(String nom) throws Exception;
}
