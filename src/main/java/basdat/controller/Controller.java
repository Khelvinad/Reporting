package basdat.controller;

import basdat.model.Instructor;
import basdat.model.dao.InstructorDAO;
import basdat.model.dao.deptDAO;
import basdat.report.reportGenerator;
import basdat.view.MainFrame;
import basdat.view.InstructorDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.List;

public class Controller {
    private MainFrame mainFrame;
    private InstructorDAO instructorDAO;
    private deptDAO departmentDAO;
    private reportGenerator reportManager;

    public Controller(MainFrame mainFrame, InstructorDAO instructorDAO, deptDAO departmentDAO) {
        this.mainFrame = mainFrame;
        this.instructorDAO = instructorDAO;
        this.departmentDAO = departmentDAO;
        this.reportManager = new reportGenerator();

        initView();
        initController();
    }

    private void initView() {
        loadInstructorData();
        mainFrame.setVisible(true);
    }

    private void initController() {
        mainFrame.getAddButton().addActionListener(e -> showAddInstructorDialog());
        mainFrame.getEditButton().addActionListener(e -> showEditInstructorDialog());
        mainFrame.getExportButton().addActionListener(e -> exportReport());
        mainFrame.getDeleteButton().addActionListener(e -> deleteSelectedInstructor());
    }

    private void loadInstructorData() {
        List<Instructor> instructors = instructorDAO.getAllInstructors();
        DefaultTableModel model = mainFrame.getTableModel();
        model.setRowCount(0);
        for (Instructor instructor : instructors) {
            model.addRow(new Object[]{
                    instructor.getId(),
                    instructor.getName(),
                    instructor.getDeptName(),
                    instructor.getSalary()
            });
        }
    }

    private void showAddInstructorDialog() {
        List<String> deptNames = departmentDAO.getAllDepartmentNames();
        if (deptNames.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Tidak ada data departemen. Harap tambahkan departemen terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        InstructorDialog dialog = new InstructorDialog(mainFrame, "Tambah Dosen", true, deptNames);
        dialog.prepareForAdd(deptNames);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Instructor newInstructor = new Instructor();
            newInstructor.setId(dialog.getIdInput());
            newInstructor.setName(dialog.getNameInput());
            newInstructor.setDeptName(dialog.getSelectedDeptName());
            newInstructor.setSalary(dialog.getSalaryInput());

            if (newInstructor.getId().isEmpty() || newInstructor.getName().isEmpty() || newInstructor.getSalary() == null) {
                JOptionPane.showMessageDialog(mainFrame, "ID, Nama dan Gaji (valid) harus diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (instructorDAO.getInstructorById(newInstructor.getId()) != null) {
                JOptionPane.showMessageDialog(mainFrame, "ID Dosen sudah ada.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (instructorDAO.addInstructor(newInstructor)) {
                loadInstructorData();
                JOptionPane.showMessageDialog(mainFrame, "Data dosen berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Gagal menambahkan data dosen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditInstructorDialog() {
        String selectedId = mainFrame.getSelectedInstructorId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(mainFrame, "Pilih dosen yang akan diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Instructor instructorToEdit = instructorDAO.getInstructorById(selectedId);
        if (instructorToEdit == null) {
            JOptionPane.showMessageDialog(mainFrame, "Data dosen tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            loadInstructorData();
            return;
        }

        List<String> deptNames = departmentDAO.getAllDepartmentNames();
        InstructorDialog dialog = new InstructorDialog(mainFrame, "Edit Dosen", true, deptNames);
        dialog.prepareForEdit(
                instructorToEdit.getId(),
                instructorToEdit.getName(),
                instructorToEdit.getDeptName(),
                instructorToEdit.getSalary(),
                deptNames
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Instructor updatedInstructor = new Instructor();
            updatedInstructor.setId(dialog.getCurrentIdForEdit());
            updatedInstructor.setName(dialog.getNameInput());
            updatedInstructor.setDeptName(dialog.getSelectedDeptName());
            updatedInstructor.setSalary(dialog.getSalaryInput());

            if (updatedInstructor.getName().isEmpty() || updatedInstructor.getSalary() == null) {
                JOptionPane.showMessageDialog(mainFrame, "Nama dan Gaji (valid) harus diisi.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (instructorDAO.updateInstructor(updatedInstructor)) {
                loadInstructorData();
                JOptionPane.showMessageDialog(mainFrame, "Data dosen berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Gagal memperbarui data dosen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedInstructor() {
        String selectedId = mainFrame.getSelectedInstructorId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(mainFrame, "Pilih dosen yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(mainFrame,
                "Apakah Anda yakin ingin menghapus dosen dengan ID: " + selectedId + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (instructorDAO.deleteInstructor(selectedId)) {
                loadInstructorData(); // Refresh tabel
                JOptionPane.showMessageDialog(mainFrame, "Data dosen berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Gagal menghapus data dosen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportReport() {
        List<Instructor> instructors = instructorDAO.getAllInstructors();
        if (instructors.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Tidak ada data untuk diekspor.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Workbook (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File("Laporan_Dosen.xlsx"));

        int userSelection = fileChooser.showSaveDialog(mainFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String outputPath = fileToSave.getAbsolutePath();
            if (!outputPath.toLowerCase().endsWith(".xlsx")) {
                outputPath += ".xlsx";
            }

            try {
                String jrxmlPath = "templates/instructor_report.jrxml";
                reportManager.exportToExcel(instructors, jrxmlPath, outputPath);
                JOptionPane.showMessageDialog(mainFrame, "Laporan berhasil diekspor ke:\n" + outputPath, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "Gagal mengekspor laporan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
