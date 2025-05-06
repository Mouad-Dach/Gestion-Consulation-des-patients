package net.dach.gestiondespatients.enitities;



import java.util.Date;

public class Consultation {
    private int id;
    private int patientId;
    private Date dateConsultation;
    private String motif;
    private String statut;

    // Constructeur, getters, setters
    public Consultation(int id, int patientId, Date dateConsultation, String motif, String statut) {
        this.id = id;
        this.patientId = patientId;
        this.dateConsultation = dateConsultation;
        this.motif = motif;
        this.statut = statut;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Date getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(Date dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
