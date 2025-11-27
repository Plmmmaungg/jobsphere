package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutUsController {

    @FXML
    private void onDashboardclick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("companyCreatedDashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    @FXML
    private void onPositionClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("companyCreatedPosition.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    @FXML
    private void onsignOutButton(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }
    @FXML
    private void onAboutUs(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("companyCreatedAboutUs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

}
