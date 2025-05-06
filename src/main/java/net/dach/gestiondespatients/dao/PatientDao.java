package net.dach.gestiondespatients.dao;


import net.dach.gestiondespatients.enitities.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDao implements IPatientDao {
    @Override
    public List<Patient> findAll() throws Exception {
        List<Patient> patients = new ArrayList<>();
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            return patients; // Return empty list if connection fails
        }

        String sql = "SELECT * FROM patients";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance"),
                        rs.getString("telephone"),
                        rs.getString("adresse")
                ));
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return patients;
    }

    @Override
    public List<Patient> searchByNom(String nom) throws Exception {
        List<Patient> patients = new ArrayList<>();
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            return patients; // Return empty list if connection fails
        }

        String sql = "SELECT * FROM patients WHERE nom LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(new Patient(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getDate("date_naissance"),
                            rs.getString("telephone"),
                            rs.getString("adresse")
                    ));
                }
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return patients;
    }

    @Override
    public Patient findById(int id) throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            return null; // Return null if connection fails
        }

        String sql = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getDate("date_naissance"),
                            rs.getString("telephone"),
                            rs.getString("adresse")
                    );
                }
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Finds the smallest available ID that's not currently in use
     * @return the smallest available ID
     * @throws Exception if there's an error accessing the database
     */
    private int findSmallestAvailableId() throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        try {
            // Get all existing IDs
            List<Integer> existingIds = new ArrayList<>();
            String sql = "SELECT id FROM patients ORDER BY id";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    existingIds.add(rs.getInt("id"));
                }
            }

            // Find the smallest available ID
            int smallestId = 1;
            for (Integer existingId : existingIds) {
                if (existingId == smallestId) {
                    smallestId++;
                } else if (existingId > smallestId) {
                    // Found a gap
                    break;
                }
            }

            return smallestId;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public void save(Patient patient) throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        // Find the smallest available ID
        int newId = findSmallestAvailableId();

        // Include the ID in the INSERT statement
        String sql = "INSERT INTO patients (id, nom, prenom, date_naissance, telephone, adresse) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newId);
            stmt.setString(2, patient.getNom());
            stmt.setString(3, patient.getPrenom());
            stmt.setDate(4, new java.sql.Date(patient.getDateNaissance().getTime()));
            stmt.setString(5, patient.getTelephone());
            stmt.setString(6, patient.getAdresse());
            stmt.executeUpdate();

            // Update the patient object with the new ID
            patient.setId(newId);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public void update(Patient patient) throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        String sql = "UPDATE patients SET nom = ?, prenom = ?, date_naissance = ?, telephone = ?, adresse = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, new java.sql.Date(patient.getDateNaissance().getTime()));
            stmt.setString(4, patient.getTelephone());
            stmt.setString(5, patient.getAdresse());
            stmt.setInt(6, patient.getId());
            stmt.executeUpdate();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(int id) throws Exception {
        // First, delete all consultations associated with this patient
        IConsultationDao consultationDao = new ConsultationDao();
        consultationDao.deleteByPatientId(id);

        // Then delete the patient
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        String sql = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
