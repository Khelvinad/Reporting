package basdat.view;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class InstructorDialog extends JDialog {
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> deptNameComboBox;
    private JTextField salaryField;
    private JButton saveButton;
    private JButton cancelButton;

    private boolean confirmed = false;
    private String currentIdForEdit = null;

    public InstructorDialog(Frame owner, String title, boolean modal, List<String> departmentNames) {
        super(owner, title, modal);
        initComponents(departmentNames);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(List<String> departmentNames) {
        idField = new JTextField(10);
        nameField = new JTextField(20);
        deptNameComboBox = new JComboBox<>(departmentNames.toArray(new String[0]));
        salaryField = new JTextField(10);

        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");

        saveButton.addActionListener(e -> {
            if (getNameInput().isEmpty() || getSalaryInput() == null) {
                JOptionPane.showMessageDialog(this, "Nama dan Gaji tidak boleh kosong, dan Gaji harus angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (getIdInput().isEmpty() && currentIdForEdit == null) {
                JOptionPane.showMessageDialog(this, "ID tidak boleh kosong.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmed = true;
            setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Departemen:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(deptNameComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Gaji:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(salaryField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    public void prepareForAdd(List<String> departmentNames) {
        setTitle("Tambah Dosen");
        idField.setText("");
        idField.setEnabled(true);
        nameField.setText("");
        salaryField.setText("");
        if (!departmentNames.isEmpty()) deptNameComboBox.setSelectedIndex(0);
        currentIdForEdit = null;
        confirmed = false;
    }

    public void prepareForEdit(String id, String name, String deptName, BigDecimal salary, List<String> departmentNames) {
        setTitle("Edit Dosen");
        currentIdForEdit = id;
        idField.setText(id);
        idField.setEnabled(false);
        nameField.setText(name);
        deptNameComboBox.setSelectedItem(deptName);
        salaryField.setText(salary != null ? salary.toPlainString() : "");
        confirmed = false;
    }


    public boolean isConfirmed() { return confirmed; }
    public String getIdInput() { return idField.getText().trim(); }
    public String getNameInput() { return nameField.getText().trim(); }
    public String getSelectedDeptName() { return (String) deptNameComboBox.getSelectedItem(); }
    public BigDecimal getSalaryInput() {
        try {
            return new BigDecimal(salaryField.getText().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public String getCurrentIdForEdit() { return currentIdForEdit; }
}
