package basdat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {
    private JTable instructorTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, exportButton, deleteButton;

    public MainWindow() {
        setTitle("University Instructor Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setLayout(new GridLayout(1, 1));
        headerPanel.add(new JLabel("Instructor List", JLabel.CENTER));
        headerPanel.setBackground(Color.BLACK);
        JLabel headerLabel = (JLabel) headerPanel.getComponent(0);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        headerLabel.setForeground(Color.WHITE);

        String[] columnNames = {"ID", "Name", "Department", "Salary"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        instructorTable = new JTable(tableModel);
        instructorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instructorTable.setFillsViewportHeight(true);
        instructorTable.setRowHeight(25);
        instructorTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        instructorTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instructorTable.setSelectionBackground(Color.BLACK);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Add New");
        editButton = new JButton("Edit Selected");
        deleteButton = new JButton("Delete Selected");
        exportButton = new JButton("Export to Excel");

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 12);
        for (JButton btn : new JButton[]{addButton, editButton, deleteButton, exportButton}) {
            btn.setFont(buttonFont);
        }

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);

        setLayout(new BorderLayout(10, 10));
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(instructorTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JTable getInstructorTable() { return instructorTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getExportButton() { return exportButton; }
    public JButton getDeleteButton() { return deleteButton; }

    public String getSelectedInstructorId() {
        int selectedRow = instructorTable.getSelectedRow();
        if (selectedRow != -1) {
            return (String) tableModel.getValueAt(selectedRow, 0);
        }
        return null;
    }
}