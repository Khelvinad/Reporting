import basdat.controller.Controller;
import basdat.db.dbConnection;
import basdat.model.dao.deptDAO;
import basdat.model.dao.InstructorDAO;
import basdat.view.MainFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            dbConnection.getConnection();
            System.out.println("Koneksi database berhasil!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal terhubung ke database: " + e.getMessage() +
                            "\nPastikan SQL Server berjalan dan konfigurasi sudah benar.",
                    "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            InstructorDAO instructorDAO = new InstructorDAO();
            deptDAO departmentDAO = new deptDAO();
            new Controller(mainFrame, instructorDAO, departmentDAO);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(dbConnection::closeConnection));
    }
}
