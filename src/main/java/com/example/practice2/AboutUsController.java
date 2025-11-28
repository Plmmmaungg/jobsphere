package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutUsController {

    @FXML
    private Label companyNameLabel;

    @FXML
    private ImageView companyLogoView;

    private int companyId;
    private String companyName;
    private Image companyLogo;

    // RECEIVES COMPANY DATA
    public void setCompanyData(int id, String name, Image logo) {
        this.companyId = id;
        this.companyName = name;
        this.companyLogo = logo;

        if (companyNameLabel != null)
            companyNameLabel.setText(name);

        if (companyLogoView != null)
            companyLogoView.setImage(logo);
    }

    // ----------------- DASHBOARD -----------------
    @FXML
    private void onDashboardclick(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedDashboard.fxml"));
        Parent root = loader.load();

        CreatedAdminCompanyController controller = loader.getController();
        controller.setCompanyData(companyId, companyName, companyLogo);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ----------------- POSITION PAGE -----------------
    @FXML
    private void onPositionClick(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedPosition.fxml"));
        Parent root = loader.load();

        PositionController controller = loader.getController();
        controller.setCompanyData(companyId, companyName, companyLogo);  // FIXED

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ----------------- SIGN OUT -----------------
    @FXML
    private void onsignOutButton(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    // ----------------- ABOUT US (REFRESH) -----------------
    @FXML
    private void onAboutUs(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedAboutUs.fxml"));
        Parent root = loader.load();

        AboutUsController controller = loader.getController();
        controller.setCompanyData(companyId, companyName, companyLogo);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
