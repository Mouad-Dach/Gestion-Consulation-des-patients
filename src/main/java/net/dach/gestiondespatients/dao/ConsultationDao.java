package net.dach.gestiondespatients.dao;


import net.dach.gestiondespatients.enitities.Consultation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDao implements IConsultationDao {
    @Override
    public List<Consultation> findAll() throws Exception {
        List<Consultation> consultations = new ArrayList<>();
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            return consultations; // Return empty list if connection fails
        }

        String sql = "SELECT * FROM consultations";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                consultations.add(new Consultation(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getDate("date_consultation"),
                        rs.getString("motif"),
                        rs.getString("statut")
                ));
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByPatientId(int patientId) throws Exception {
        List<Consultation> consultations = new ArrayList<>();
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            return consultations; // Return empty list if connection fails
        }

        String sql = "SELECT * FROM consultations WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultations.add(new Consultation(
                            rs.getInt("id"),
                            rs.getInt("patient_id"),
                            rs.getDate("date_consultation"),
                            rs.getString("motif"),
                            rs.getString("statut")
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
        return consultations;
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
            String sql = "SELECT id FROM consultations ORDER BY id";
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
    public void save(Consultation consultation) throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        // Find the smallest available ID
        int newId = findSmallestAvailableId();

        // Include the ID in the INSERT statement
        String sql = "INSERT INTO consultations (id, patient_id, date_consultation, motif, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newId);
            stmt.setInt(2, consultation.getPatientId());
            stmt.setDate(3, new java.sql.Date(consultation.getDateConsultation().getTime()));
            stmt.setString(4, consultation.getMotif());
            stmt.setString(5, consultation.getStatut());
            stmt.executeUpdate();

            // Update the consultation object with the new ID
            consultation.setId(newId);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public Consultation findById(int id) throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            return null; // Return null if connection fails
        }

        String sql = "SELECT * FROM consultations WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Consultation(
                            rs.getInt("id"),
                            rs.getInt("patient_id"),
                            rs.getDate("date_consultation"),
                            rs.getString("motif"),
                            rs.getString("statut")
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

    @Override
    public void update(Consultation consultation) throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        String sql = "UPDATE consultations SET patient_id = ?, date_consultation = ?, motif = ?, statut = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consultation.getPatientId());
            stmt.setDate(2, new java.sql.Date(consultation.getDateConsultation().getTime()));
            stmt.setString(3, consultation.getMotif());
            stmt.setString(4, consultation.getStatut());
            stmt.setInt(5, consultation.getId());
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
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        String sql = "DELETE FROM consultations WHERE id = ?";
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

    @Override
    public void deleteByPatientId(int patientId) throws Exception {
        Connection conn = DbConnection.getConnection();
        if (conn == null) {
            throw new Exception("Unable to connect to database");
        }

        String sql = "DELETE FROM consultations WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
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
