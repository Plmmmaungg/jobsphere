package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private void loadDeletedCompanies() {
        // Load deleted companies and add cards to displayDeletedCard
    }

    @FXML
    private void onDashboardButtonClick(MouseEvent event) throws IOException {
        Parent dashboardRoot = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(dashboardRoot));
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
