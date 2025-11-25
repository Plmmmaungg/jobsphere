package com.example.practice2;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;


    private PauseTransition hideDelay;

    // 🔹 REGISTER button logic
    @FXML
    private void onRegisterClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (hideDelay != null) hideDelay.stop();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.1), statusLabel);
        fadeOut.setFromValue(0.5);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            if (username.isEmpty() || password.isEmpty()) {
                showStatus("⚠ Please fill in all fields.", Color.ORANGE);
                return;
            }

            // Insert into database
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();

                showStatus("✅ Registration successful!", Color.GREEN);

                PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
                delay.setOnFinished(ev -> goToLogin(event));
                delay.play();

            } catch (SQLException ex) {
                if (ex.getMessage().contains("Duplicate entry")) {
                    showStatus("⚠ Username already exists.", Color.ORANGE);
                } else {
                    ex.printStackTrace();
                    showStatus("⚠ Database error.", Color.RED);
                }
            }
        });

        fadeOut.play();
    }


    // 🔹 Helper: go back to login page
    @FXML
    private void onBackToLoginClick(ActionEvent event) {
        goToLogin(event);
    }

    private void goToLogin(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Helper: show animated status
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.1), statusLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.5);
        fadeIn.play();

        hideDelay = new PauseTransition(Duration.seconds(3));
        hideDelay.setOnFinished(event -> statusLabel.setText(""));
        hideDelay.play();
    }
    @FXML
    private void onBackButtonClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

}
