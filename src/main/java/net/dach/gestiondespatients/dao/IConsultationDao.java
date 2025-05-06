package net.dach.gestiondespatients.dao;

import net.dach.gestiondespatients.enitities.Consultation;
import java.util.List;

public interface IConsultationDao extends Dao<Consultation> {
    List<Consultation> findByPatientId(int patientId) throws Exception;
    void deleteByPatientId(int patientId) throws Exception;
}
