package basdat.model.dao;

import basdat.db.dbConnection;
import basdat.model.dept;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class deptDAO {

    public List<String> getAllDepartmentNames() {
        List<String> departmentNames = new ArrayList<>();
        String sql = "SELECT dept_name FROM department ORDER BY dept_name";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departmentNames.add(rs.getString("dept_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departmentNames;
    }

    public List<dept> getAllDepartments() {
        List<dept> departments = new ArrayList<>();
        String sql = "SELECT dept_name, building, budget FROM department ORDER BY dept_name";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(new dept(rs.getString("dept_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }
}