package basdat.logic;

import basdat.entity.InstructorRecord;
import basdat.repository.InstructorRepository;
import basdat.repository.DepartmentRepository;
import basdat.reporting.ExcelReportGenerator;
import basdat.ui.MainWindow;
import basdat.ui.InstructorFormDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.List;

public class ApplicationController {
    private MainWindow mainWindow;
    private InstructorRepository instructorRepository;
    private DepartmentRepository departmentRepository;
    private ExcelReportGenerator reportGenerator;

    public ApplicationController(MainWindow mainWindow, InstructorRepository instructorRepository, DepartmentRepository departmentRepository) {
        this.mainWindow = mainWindow;
        this.instructorRepository = instructorRepository;
        this.departmentRepository = departmentRepository;
        this.reportGenerator = new ExcelReportGenerator();

        initView();
        initController();
    }

    private void initView() {
        loadInstructorData();
        mainWindow.setVisible(true);
    }

    private void initController() {
        mainWindow.getAddButton().addActionListener(e -> showAddInstructorDialog());
        mainWindow.getEditButton().addActionListener(e -> showEditInstructorDialog());
        mainWindow.getExportButton().addActionListener(e -> exportReport());
        mainWindow.getDeleteButton().addActionListener(e -> deleteSelectedInstructor());
    }

    private void loadInstructorData() {
        List<InstructorRecord> instructors = instructorRepository.getAllInstructors();
        DefaultTableModel model = mainWindow.getTableModel();
        model.setRowCount(0);
        for (InstructorRecord instructor : instructors) {
            model.addRow(new Object[]{
                    instructor.getId(),
                    instructor.getName(),
                    instructor.getDeptName(),
                    instructor.getSalary()
            });
        }
    }

    private void showAddInstructorDialog() {
        List<String> deptNames = departmentRepository.getAllDepartmentNames();
        if (deptNames.isEmpty()) {
            JOptionPane.showMessageDialog(mainWindow, "No department data found. Please add departments first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        InstructorFormDialog dialog = new InstructorFormDialog(mainWindow, "Add Instructor", true, deptNames);
        dialog.prepareForAdd(deptNames);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            InstructorRecord newInstructor = new InstructorRecord();
            newInstructor.setId(dialog.getIdInput());
            newInstructor.setName(dialog.getNameInput());
            newInstructor.setDeptName(dialog.getSelectedDeptName());
            newInstructor.setSalary(dialog.getSalaryInput());

            if (newInstructor.getId().isEmpty() || newInstructor.getName().isEmpty() || newInstructor.getSalary() == null) {
                JOptionPane.showMessageDialog(mainWindow, "ID, Name, and a valid Salary must be provided.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (instructorRepository.getInstructorById(newInstructor.getId()) != null) {
                JOptionPane.showMessageDialog(mainWindow, "Instructor ID already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (instructorRepository.addInstructor(newInstructor)) {
                loadInstructorData();
                JOptionPane.showMessageDialog(mainWindow, "Instructor data added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Failed to add instructor data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditInstructorDialog() {
        String selectedId = mainWindow.getSelectedInstructorId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(mainWindow, "Please select an instructor to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        InstructorRecord instructorToEdit = instructorRepository.getInstructorById(selectedId);
        if (instructorToEdit == null) {
            JOptionPane.showMessageDialog(mainWindow, "Instructor data not found.", "Error", JOptionPane.ERROR_MESSAGE);
            loadInstructorData();
            return;
        }

        List<String> deptNames = departmentRepository.getAllDepartmentNames();
        InstructorFormDialog dialog = new InstructorFormDialog(mainWindow, "Edit Instructor", true, deptNames);
        dialog.prepareForEdit(
                instructorToEdit.getId(),
                instructorToEdit.getName(),
                instructorToEdit.getDeptName(),
                instructorToEdit.getSalary(),
                deptNames
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            InstructorRecord updatedInstructor = new InstructorRecord();
            updatedInstructor.setId(dialog.getCurrentIdForEdit());
            updatedInstructor.setName(dialog.getNameInput());
            updatedInstructor.setDeptName(dialog.getSelectedDeptName());
            updatedInstructor.setSalary(dialog.getSalaryInput());

            if (updatedInstructor.getName().isEmpty() || updatedInstructor.getSalary() == null) {
                JOptionPane.showMessageDialog(mainWindow, "Name and a valid Salary must be provided.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (instructorRepository.updateInstructor(updatedInstructor)) {
                loadInstructorData();
                JOptionPane.showMessageDialog(mainWindow, "Instructor data updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Failed to update instructor data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedInstructor() {
        String selectedId = mainWindow.getSelectedInstructorId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(mainWindow, "Please select an instructor to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(mainWindow,
                "Are you sure you want to delete instructor with ID: " + selectedId + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (instructorRepository.deleteInstructor(selectedId)) {
                loadInstructorData();
                JOptionPane.showMessageDialog(mainWindow, "Instructor data deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Failed to delete instructor data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportReport() {
        List<InstructorRecord> instructors = instructorRepository.getAllInstructors();
        if (instructors.isEmpty()) {
            JOptionPane.showMessageDialog(mainWindow, "No data to export.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel Report");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Workbook (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File("Instructor_Report.xlsx"));

        int userSelection = fileChooser.showSaveDialog(mainWindow);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String outputPath = fileToSave.getAbsolutePath();
            if (!outputPath.toLowerCase().endsWith(".xlsx")) {
                outputPath += ".xlsx";
            }

            try {
                String jrxmlPath = "templates/instructor_report.jrxml";
                reportGenerator.exportToExcel(instructors, jrxmlPath, outputPath);
                JOptionPane.showMessageDialog(mainWindow, "Report exported successfully to:\n" + outputPath, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainWindow, "Failed to export report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}