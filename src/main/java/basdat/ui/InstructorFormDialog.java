package basdat.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InstructorFormDialog extends JDialog {
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> deptNameComboBox;
    private JTextField salaryField;
    private JButton saveButton;
    private JButton cancelButton;

    private boolean confirmed = false;
    private String currentIdForEdit = null;

    public InstructorFormDialog(Frame owner, String title, boolean modal, List<String> departmentNames) {
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

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        saveButton.addActionListener(e -> {
            if (getNameInput().isEmpty() || getSalaryInput() == null) {
                JOptionPane.showMessageDialog(this, "Name and Salary must not be empty, and Salary must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (getIdInput().isEmpty() && currentIdForEdit == null) {
                JOptionPane.showMessageDialog(this, "ID must not be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
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

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(deptNameComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(salaryField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    public void prepareForAdd(List<String> departmentNames) {
        setTitle("Add Instructor");
        idField.setText("");
        idField.setEnabled(true);
        nameField.setText("");
        salaryField.setText("");
        if (deptNameComboBox.getItemCount() > 0 && !departmentNames.isEmpty()) {
             deptNameComboBox.setSelectedIndex(0);
        } else if (deptNameComboBox.getItemCount() == 0 && departmentNames != null && !departmentNames.isEmpty()) {
            deptNameComboBox.removeAllItems();
            for (String deptName : departmentNames) {
                deptNameComboBox.addItem(deptName);
            }
            if (deptNameComboBox.getItemCount() > 0) deptNameComboBox.setSelectedIndex(0);
        }
        currentIdForEdit = null;
        confirmed = false;
    }

    public void prepareForEdit(String id, String name, String deptName, BigDecimal salary, List<String> departmentNames) {
        setTitle("Edit Instructor");
        currentIdForEdit = id;
        idField.setText(id);
        idField.setEnabled(false);
        nameField.setText(name);
        if(departmentNames != null) {
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) deptNameComboBox.getModel();
            model.removeAllElements();
            for (String dn : departmentNames) {
                model.addElement(dn);
            }
        }
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
            String text = salaryField.getText().trim();
            if (text.isEmpty()) return null;
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public String getCurrentIdForEdit() { return currentIdForEdit; }
}