package net.dach.gestiondespatients.enitities;

/**
 * This class contains constants for the different consultation statuses.
 */
public class StatutConsultation {
    // Statut constants
    public static final String EN_ATTENTE = "En attente";
    public static final String EN_COURS = "En cours";
    public static final String CONSULTE = "Consulté";
    public static final String ABSENT = "Absent";
    public static final String ANNULE = "Annulé";
    public static final String REPORTE = "Reporté";
    public static final String URGENT = "Urgent";
    public static final String SUIVI = "Suivi";
    public static final String CLOTURE = "Clôturé";
    public static final String NOUVEAU_PATIENT = "Nouveau patient";
    public static final String RECONSULTATION = "Reconsultation";
    public static final String TELECONSULTATION = "Téléconsultation";
    public static final String NON_VENU = "Non venu";
    public static final String EN_OBSERVATION = "En observation";
    public static final String EXAMEN_COMPLEMENTAIRE = "Examen complémentaire";
    
    /**
     * Returns a list of all available consultation statuses.
     * @return Array of all consultation statuses
     */
    public static String[] getAllStatuts() {
        return new String[] {
            EN_ATTENTE,
            EN_COURS,
            CONSULTE,
            ABSENT,
            ANNULE,
            REPORTE,
            URGENT,
            SUIVI,
            CLOTURE,
            NOUVEAU_PATIENT,
            RECONSULTATION,
            TELECONSULTATION,
            NON_VENU,
            EN_OBSERVATION,
            EXAMEN_COMPLEMENTAIRE
        };
    }
}