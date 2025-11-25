package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UserInboxController {
    @FXML
    private void onSignOutClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }
    @FXML
    private void onUserDashboardClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("userDashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }
    @FXML
    private void onUserProfileClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("userProfileDashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }
    @FXML
    private Label usernameLabel; // this is your label showing @username

    public void setUsername(String username) {
        usernameLabel.setText("@" + username);
    }
}


