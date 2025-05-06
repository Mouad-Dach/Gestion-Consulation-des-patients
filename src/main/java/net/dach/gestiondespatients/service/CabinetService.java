package net.dach.gestiondespatients.service;



import net.dach.gestiondespatients.dao.PatientDao;
import net.dach.gestiondespatients.enitities.Patient;
import java.util.List;

public class CabinetService implements ICabinetService {
    private final PatientDao patientDao = new PatientDao();

    @Override
    public List<Patient> getAllPatients() throws Exception {
        return patientDao.findAll();
    }

    @Override
    public void addPatient(Patient patient) throws Exception {
        patientDao.save(patient);
    }

    @Override
    public void updatePatient(Patient patient) throws Exception {
        patientDao.update(patient);
    }

    @Override
    public void deletePatient(int id) throws Exception {
        patientDao.delete(id);
    }

    @Override
    public List<Patient> searchPatientsByNom(String nom) throws Exception {
        return patientDao.searchByNom(nom);
    }
}
