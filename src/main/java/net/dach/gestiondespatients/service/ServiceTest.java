package net.dach.gestiondespatients.service;


import net.dach.gestiondespatients.enitities.Patient;
import java.util.Date;

public class ServiceTest {
    public static void main(String[] args) {
        ICabinetService service = new CabinetService();
        try {
            service.addPatient(new Patient(0, "Dupont", "Jean", new Date(), "0600000000", "Paris"));
            System.out.println("Patient ajouté !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
