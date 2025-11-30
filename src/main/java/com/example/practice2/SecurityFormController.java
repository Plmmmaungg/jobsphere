package com.example.practice2;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;

import java.io.IOException;

public class SecurityFormController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView logoPreview;

    @FXML
    private Button backButton;

    private int companyId;

    public void setCompanyId(int id) {
        this.companyId = id;
    }

    @FXML
    private AnchorPane securityFormContainer;
    private Runnable onLoginSuccess;
    public void setOnLoginSuccess(Runnable r) {
        this.onLoginSuccess = r;
    }


    // 🔹 Data passed from AdminController
    private String companyName;
    private String username;
    private String password;
    private Image logoImage;
    private PauseTransition hideDelay;

    private AdminController parentController;
    // 🔹 References to AdminController containers
    private AnchorPane addCompanyForm;
    private AnchorPane securityFormContainerInAdmin;
    private HBox displayCardCompany;

    // ------------------------------------------------------------
    // 🔹 This method is called from AdminController
    // ------------------------------------------------------------
    public void setContainers(AnchorPane addCompanyForm, AnchorPane securityFormContainerInAdmin) {
        this.addCompanyForm = addCompanyForm;
        this.securityFormContainerInAdmin = securityFormContainerInAdmin;
    }


    // ------------------------------------------------------------
    // 🔹 Sets data when opening the Security Form
    // ------------------------------------------------------------
    public void setCompanyData(int id, String name, String user, String pass, Image logo) {
        this.companyId = id;
        this.companyName = name;
        this.username = user;
        this.password = pass;
        this.logoImage = logo;

        usernameField.setText("");
        passwordField.setText("");

        if (logo != null) {
            logoPreview.setImage(logo);
        }
    }



    public void setParentController(AdminController controller) {
        this.parentController = controller;
    }

    // ------------------------------------------------------------
    // 🔹 Handles BACK button click
    // ------------------------------------------------------------
    @FXML
    private void onBackButtonClick() {
        if (parentController != null) {
            parentController.showDashboardView();
        }
    }


    // ------------------------------------------------------------
    // 🔹 Handles CONTINUE button click (keep your logic)
    // ------------------------------------------------------------
    @FXML
    private void onContinueButtonClick() throws IOException {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = passwordField.getText().trim();

        if (hideDelay != null) hideDelay.stop();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showError("⚠ Please fill in all fields.", Color.ORANGE);
            return;
        }

        if (!enteredUsername.equals(username) || !enteredPassword.equals(password)) {
            showError("❌ Invalid username or password.", Color.RED);
            return;
        }

        if (onLoginSuccess != null) {
            onLoginSuccess.run();
        }



        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedDashboard.fxml"));
        Parent root = loader.load();

        CreatedAdminCompanyController controller = loader.getController();
        controller.setCompanyData(companyId, companyName, logoImage);

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }





    private void showError(String message, Color color) {
        errorLabel.setText(message);
        errorLabel.setTextFill(color);
        errorLabel.setVisible(true);

        // Fade in
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2), errorLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Auto-hide after 3 seconds
        hideDelay = new PauseTransition(Duration.seconds(3));
        hideDelay.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.2), errorLabel);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> errorLabel.setText(""));
            fadeOut.play();
        });
        hideDelay.play();
    }

}
