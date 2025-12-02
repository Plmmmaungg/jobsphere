package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
public class UserInboxDashboardController {

    @FXML private VBox viewMessageBox;
    @FXML private TextFlow viewMessage;
    @FXML private Label usernameLabel;

    private int currentUserId = -1;   // FIX: avoid triggering loadMessages with empty ID
    private int companyId;
    private String currentUserEmail;



    public void setCompanyId(int id) { this.companyId = id; }

    public void setUsername(String username) {
        this.currentUserEmail = username;
        usernameLabel.setText("@" + username);
    }

    @FXML
    private void onSignOutClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    @FXML
    private void onUserDashboardClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userDashboard.fxml"));
        Parent root = loader.load();

        UserDashboardController controller = loader.getController();
        controller.setUsername(currentUserEmail);
        controller.setCompanyId(companyId);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onUserProfileClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userProfileDashboard.fxml"));
        Parent root = loader.load();

        UserProfileController controller = loader.getController();
        controller.setUsername(currentUserEmail);
        controller.setCompanyId(companyId);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
