package com.example.practice2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserProfileController {

    @FXML private Label viewName;
    @FXML private Label viewAge;
    @FXML private Label viewGender;
    @FXML private Label viewNationality;
    @FXML private Label viewStatus;
    @FXML private Label viewReligion;
    @FXML private Label viewContact;
    @FXML private Label viewEmail;
    @FXML private Label viewAddress;
    @FXML private Label usernameLabel;

    @FXML private ImageView view2x2;
    @FXML private ImageView viewFile;

    private String currentUserEmail;

    public void setUsername(String username) {
        this.currentUserEmail = username;
        usernameLabel.setText("@" + username);
        loadUserApplicantData();
    }


    private int companyId;

    public void setCompanyId(int id) {
        this.companyId = id;
        loadLatestApplicantData();
    }

    private void loadUserApplicantData() {
        String sql = "SELECT * FROM applicants WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, getLoggedInUserId(currentUserEmail));  // <-- key part
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fillProfileFields(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCompanyApplicantData() {
        String sql = "SELECT * FROM applicants WHERE company_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fillProfileFields(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillProfileFields(ResultSet rs) throws Exception {
        viewName.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
        viewAge.setText(rs.getString("age"));
        viewGender.setText(rs.getString("gender"));
        viewNationality.setText(rs.getString("nationality"));
        viewStatus.setText("Pending");
        viewReligion.setText(rs.getString("religion"));

        viewContact.setText("Phone: " + rs.getString("contact"));
        viewEmail.setText("E-mail: " + rs.getString("email"));
        viewAddress.setText("Address: " + rs.getString("address"));

        String picturePath = rs.getString("picture_path");
        if (picturePath != null && new File(picturePath).exists()) {
            view2x2.setImage(new Image(new File(picturePath).toURI().toString()));
        } else {
            view2x2.setImage(null);
        }
    }

    private int getLoggedInUserId(String username) throws Exception {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : 0;
        }
    }


    public void loadLatestApplicantData() {
        String sql = "SELECT * FROM applicants WHERE company_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                viewName.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                viewAge.setText(rs.getString("age"));
                viewGender.setText(rs.getString("gender"));
                viewNationality.setText(rs.getString("nationality"));
                viewStatus.setText("Pending");
                viewReligion.setText(rs.getString("religion"));

                viewContact.setText("Phone: " + rs.getString("contact"));
                viewEmail.setText("E-mail: " + rs.getString("email"));
                viewAddress.setText("Address: " + rs.getString("address"));

                String picturePath = rs.getString("picture_path");
                if (picturePath != null && new File(picturePath).exists()) {
                    view2x2.setImage(new Image(new File(picturePath).toURI().toString()));
                } else {
                    view2x2.setImage(null);
                }

                String resume = rs.getString("resume_path");
                viewFile.setVisible(resume != null && new File(resume).exists());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML private void onSignOutClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    @FXML private void onUserDashboardClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userDashboard.fxml"));
        Parent root = loader.load();
        UserDashboardController controller = loader.getController();
        controller.setUsername(currentUserEmail);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML private void onUserInboxClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userInboxDashboard.fxml"));
        Parent root = loader.load();
        UserInboxDashboardController controller = loader.getController();
        controller.setUsername(currentUserEmail);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML private void onViewResume(MouseEvent event) { openFileFromDatabase("resume_path"); }
    @FXML private void onViewPhilH(MouseEvent event) { openFileFromDatabase("philhealth_path"); }

    @FXML private void onFileImageClick(MouseEvent event) { viewFile.setVisible(false); }

    private void openFileFromDatabase(String columnName) {
        String sql = "SELECT " + columnName + " FROM applicants WHERE company_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String path = rs.getString(columnName);
                if (path != null && new File(path).exists()) {
                    java.awt.Desktop.getDesktop().open(new File(path));
                } else {
                    new Alert(Alert.AlertType.WARNING, "File not found.").show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to open file.").show();
        }
    }
}
