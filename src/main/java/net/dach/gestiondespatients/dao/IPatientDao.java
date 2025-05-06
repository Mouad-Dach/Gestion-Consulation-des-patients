package net.dach.gestiondespatients.dao;

import net.dach.gestiondespatients.enitities.Patient;
import java.util.List;

public interface IPatientDao extends Dao<Patient> {
    List<Patient> searchByNom(String nom) throws Exception;
}
