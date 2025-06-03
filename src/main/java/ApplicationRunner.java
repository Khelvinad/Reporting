import basdat.logic.ApplicationController;
import basdat.database.DatabaseConnector;
import basdat.repository.DepartmentRepository;
import basdat.repository.InstructorRepository;
import basdat.ui.MainWindow;

import javax.swing.*;
import java.sql.SQLException;

public class ApplicationRunner {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            DatabaseConnector.connect();
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to the database: " + e.getMessage() +
                            "\nPlease ensure SQL Server is running and configuration is correct.",
                    "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            InstructorRepository instructorRepository = new InstructorRepository();
            DepartmentRepository departmentRepository = new DepartmentRepository();
            new ApplicationController(mainWindow, instructorRepository, departmentRepository);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnector::disconnect));
    }
}