package app.form.calculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm {
    private JPanel panel1;
    private JTextField txt_password;
    private JTextField txt_username;
    private JButton btn_login;
    private JLabel lbl_username;
    private JLabel lbl_password;
    private JButton btn_register;

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/dbCalculator";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public LoginForm() {
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txt_username.getText();
                String password = txt_password.getText();

                if (login(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");

                    hideForm();
                    CalculatorForm.View();
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Invalid username or password.");
                }
            }
        });

        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txt_username.getText();
                String password = txt_password.getText();

                if (register(username, password)) {
                    JOptionPane.showMessageDialog(null, "Registration successful!");

                    hideForm();
                    CalculatorForm.View();
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed. Please try again.");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginForm");
        frame.setContentPane(new LoginForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private boolean login(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE name = ? AND pass = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean register(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Users (name, pass) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);

                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private void hideForm(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
        if (frame != null) {
            frame.setVisible(false);
        }
    }
}
