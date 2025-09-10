/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author HP LAPTOP
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerGUI extends JFrame {
    
    static final String DB_URL = "jdbc:mysql://localhost:3306/shop_db";
    static final String USER = "root";
    static final String PASS = "";

    // GUI components
    private JTextField nameField, emailField;
    private JButton addButton, viewButton, exitButton;
    private JTextArea outputArea;

    // Constructor
    public CustomerGUI() {
        setTitle("Customer Management");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        addButton = new JButton("Add Customer");
        viewButton = new JButton("View Customers");
        exitButton = new JButton("Exit");

        inputPanel.add(addButton);
        inputPanel.add(viewButton);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(exitButton, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> insertCustomer());
        viewButton.addActionListener(e -> viewCustomers());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // Method to insert customer
    private void insertCustomer() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both name and email.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO customers (Name, Email) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                outputArea.append("Customer added: " + name + " (" + email + ")\n");
                nameField.setText("");
                emailField.setText("");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // Method to view customers
    private void viewCustomers() {
        outputArea.setText(""); // Clear previous
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("CustomerID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                outputArea.append("ID: " + id + " | Name: " + name + " | Email: " + email + "\n");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }
}