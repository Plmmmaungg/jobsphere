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

public class CreatedAdminCompanyController {

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

        loadCompanyApplicants(companyId);
    }

    // ----------------- REFRESH DASHBOARD -----------------
    public void refreshDashboard() {
        loadCompanyApplicants(companyId);
    }

    // ----------------- LOAD APPLICANTS ---------------------
    public void loadCompanyApplicants(int companyId) {

        String sql = "SELECT id, first_name, last_name, middle_initial FROM applicants WHERE company_id = ?";
        int count = 0;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            ListOfApplicant.getItems().clear();

            while (rs.next()) {
                int id = rs.getInt("id");

                String fullName = rs.getString("last_name") + ", " +
                        rs.getString("first_name") + " " +
                        rs.getString("middle_initial") + ".";

                ListOfApplicant.getItems().add(id + " - " + fullName);
                count++;
            }

            TotalOfApplicantText.setText(String.valueOf(count));

            ListOfApplicant.setOnMouseClicked(e -> {
                String selected = ListOfApplicant.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    int applicantId = Integer.parseInt(selected.split(" - ")[0]);
                    openApplicantDetails(applicantId);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------- OPEN APPLICANT DETAILS -----------------
    private void openApplicantDetails(int applicantId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedInformation.fxml"));
            Parent root = loader.load();

            ApplicationDetailsController controller = loader.getController();
            controller.loadApplicantData(applicantId);

            // SEND COMPANY DATA
            controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

            Stage stage = (Stage) ListOfApplicant.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------- BACK BUTTON ---------------------
    @FXML
    private void onBackButtonClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedDashboard.fxml"));
        Parent root = loader.load();

        CreatedAdminCompanyController controller = loader.getController();

        // RESTORE STATE
        controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

        Stage stage = (Stage) companyLogoView.getScene().getWindow();
        stage.setScene(new Scene(root));
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
