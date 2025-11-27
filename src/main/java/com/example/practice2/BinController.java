package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BinController {

    @FXML
    private VBox displayDeletedCard;

    @FXML
    private void initialize() {
        loadDeletedCompanies();
    }

    @FXML
    private Label usernameLabel;

    private String rawUsername;

    public void setAdminUsername(String username) {
        this.rawUsername = username;
        usernameLabel.setText("@" + username);
    }


    private void loadDeletedCompanies() {
        // Load deleted companies and add cards to displayDeletedCard
    }

    @FXML
    private void onDashboardButtonClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_dashboard.fxml"));
        Parent root = loader.load();
        AdminController controller = loader.getController();
        controller.setAdminUsername(rawUsername);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onBackButtonClick(MouseEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loginRoot));
        stage.show();
    }
}
