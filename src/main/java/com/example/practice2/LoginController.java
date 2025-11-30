package com.example.practice2;

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
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    // Admin login
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";

    private PauseTransition hideDelay;

    // Go to sign-up
    @FXML
    private void onSignUpClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("signuppage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    // LOGIN logic
    @FXML
    private void onLoginClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (hideDelay != null) hideDelay.stop();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.1), statusLabel);
        fadeOut.setFromValue(0.5);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event2 -> {
            if (username.isEmpty() || password.isEmpty()) {
                showStatus("⚠ Please enter your username and password.", Color.ORANGE);
                return;
            }

            // Admin login
            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_dashboard.fxml"));
                    Parent root = loader.load();

                    AdminController adminController = loader.getController();
                    adminController.setAdminUsername(username);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Admin Dashboard");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            // USER login (correct SQL)
            String sql = "SELECT id, username FROM users WHERE username = ? AND password = ?";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username.trim());
                stmt.setString(2, password.trim());


                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {

                    // login success
                    String loggedUser = rs.getString("username");

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("userDashboard.fxml"));
                    Parent root = loader.load();

                    UserDashboardController controller = loader.getController();
                    controller.setUsername(loggedUser);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("User Dashboard");
                    stage.show();

                } else {
                    showStatus("❌ Invalid username or password.", Color.RED);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                showStatus("⚠ Database error.", Color.RED);
            }
        });

        fadeOut.play();
    }

    // Status message with fade effect
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
}
