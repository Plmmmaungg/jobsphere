package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ApplicationDetailsController {

    private int companyId;
    private String companyName;
    private Image companyLogo;
    private int applicantId;

    @FXML
    private Label viewName;
    @FXML
    private Label viewAge;
    @FXML
    private Label viewGender;
    @FXML
    private Label viewContact;
    @FXML
    private Label viewEmail;
    @FXML
    private Label viewAddress;
    @FXML
    private Label viewNationality;
    @FXML
    private Label viewOccupation;
    @FXML
    private Label viewStatus;
    @FXML
    private Label viewPosition;
    @FXML
    private Label viewLocation;
    @FXML
    private Label viewBranch;
    @FXML
    private TextArea sendMessage;

    @FXML
    private ImageView view2x2Photo;
    private String companyLogoPath;

    @FXML
    private ImageView viewFile;
    @FXML
    private Button viewResume;
    @FXML
    private Button viewPhilH;

    @FXML
    private ImageView backButton;

    @FXML
    private void initialize() {


        // Hide the file viewer at start
        if (viewFile != null) {
            viewFile.setVisible(false);

            // Click image to hide it
            viewFile.setOnMouseClicked(e -> viewFile.setVisible(false));
        }
    }

    // ------------ LOAD APPLICANT DATA ----------------
    public void loadApplicantData(int applicantId) {
        this.applicantId = applicantId;
        String sql = "SELECT * FROM applicants WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                viewName.setText(rs.getString("first_name") + " "
                        + rs.getString("middle_initial ") + "."
                        + rs.getString("last_name "));

                viewAge.setText(rs.getString("age"));
                viewGender.setText(rs.getString("gender"));
                viewContact.setText(rs.getString("contact"));
                viewEmail.setText(rs.getString("email"));
                viewAddress.setText(rs.getString("address"));
                viewNationality.setText(rs.getString("nationality"));
                viewOccupation.setText(rs.getString("occupation"));
                viewStatus.setText(rs.getString("status"));
                viewPosition.setText(rs.getString("position"));
                viewLocation.setText(rs.getString("location"));
                viewBranch.setText(rs.getString("branch"));

                String photo = rs.getString("picture_path");
                if (photo != null)
                    view2x2Photo.setImage(new Image(new File(photo).toURI().toString()));

                // Store file paths inside the FXML buttons
                viewResume.setUserData(rs.getString("resume_path"));
                viewPhilH.setUserData(rs.getString("philhealth_path"));

                // Button actions
                viewResume.setOnAction(e ->
                        showFile((String) viewResume.getUserData()));

                viewPhilH.setOnAction(e ->
                        showFile((String) viewPhilH.getUserData()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- SHOW FILE IN IMAGEVIEW ----------------
    private void showFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("❌ File path is empty.");
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("❌ File not found: " + filePath);
            return;
        }

        viewFile.setImage(new Image(file.toURI().toString()));
        viewFile.setVisible(true);
    }


    public void setCompanyData(int companyId, String companyName, Image logo) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyLogo = logo;
    }

    // ----------------- SIGN OUT ----------------------
    @FXML
    private void signOutButton(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    // ----------------- BACK BUTTON -------------------
    @FXML
    private void onBackButtonClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedDashboard.fxml"));
            Parent root = loader.load();

            CreatedAdminCompanyController controller = loader.getController();

            // RESTORE COMPANY DATA
            controller.setCompanyData(companyId, companyName, companyLogo);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnSendMessage(MouseEvent event) {
        String message = sendMessage.getText().trim();

        if (message.isEmpty()) return;

        String sql = "INSERT INTO messages (applicant_id, company_id, sender, message_text) VALUES (?, ?, 'ADMIN', ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicantId);
            stmt.setInt(2, companyId);
            stmt.setString(3, message);

            stmt.executeUpdate();
            sendMessage.clear();

            // 🔥 AFTER SAVING → SWITCH TO INBOX
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminCreatedInbox.fxml"));
            Parent root = loader.load();

            adminCreatedInboxController controller = loader.getController();
            controller.setCompanyData(companyId, companyName, companyLogo); // <--- important

            // loads messages automatically in setCompanyData()

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}