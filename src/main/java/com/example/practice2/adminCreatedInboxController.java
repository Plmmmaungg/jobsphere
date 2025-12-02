package com.example.practice2;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.IOException;

public class adminCreatedInboxController {

    @FXML private Label TotalOfApplicantText;
    @FXML private ListView<String> ListOfApplicant;
    @FXML private Label companyNameLabel;
    @FXML private ImageView companyLogoView;

    private int companyId;

    // ----------------- SET COMPANY DATA -----------------
    public void setCompanyData(int companyId, String companyName, Image logo) {
        this.companyId = companyId;
        companyNameLabel.setText(companyName);

        if (logo != null)
            companyLogoView.setImage(logo);


    }





    @FXML
    private void onPositionClick(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedPosition.fxml"));
        Parent root = loader.load();

        PositionController controller = loader.getController();
        controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
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
    private void onInboxClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminCreatedInbox.fxml"));
        Parent root = loader.load();

        PositionController controller = loader.getController();
        controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void onDashboardClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedDashboard.fxml"));
        Parent root = loader.load();

        adminCreatedInboxController controller = loader.getController();
        controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}
