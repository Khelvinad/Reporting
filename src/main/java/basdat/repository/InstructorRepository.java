package basdat.repository;

import basdat.database.DatabaseConnector;
import basdat.entity.InstructorRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorRepository {

    public List<InstructorRecord> getAllInstructors() {
        List<InstructorRecord> instructors = new ArrayList<>();
        String sql = "SELECT ID, name, dept_name, salary FROM instructor";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                instructors.add(new InstructorRecord(
                        rs.getString("ID"),
                        rs.getString("name"),
                        rs.getString("dept_name"),
                        rs.getBigDecimal("salary")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructors;
    }

    public boolean addInstructor(InstructorRecord instructor) {
        String sql = "INSERT INTO instructor (ID, name, dept_name, salary) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, instructor.getId());
            pstmt.setString(2, instructor.getName());
            pstmt.setString(3, instructor.getDeptName());
            pstmt.setBigDecimal(4, instructor.getSalary());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInstructor(InstructorRecord instructor) {
        String sql = "UPDATE instructor SET name = ?, dept_name = ?, salary = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, instructor.getName());
            pstmt.setString(2, instructor.getDeptName());
            pstmt.setBigDecimal(3, instructor.getSalary());
            pstmt.setString(4, instructor.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public InstructorRecord getInstructorById(String id) {
        String sql = "SELECT ID, name, dept_name, salary FROM instructor WHERE ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new InstructorRecord(
                            rs.getString("ID"),
                            rs.getString("name"),
                            rs.getString("dept_name"),
                            rs.getBigDecimal("salary")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteInstructor(String id) {
        String sql = "DELETE FROM instructor WHERE ID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}