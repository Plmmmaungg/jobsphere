package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDashboardController {

    @FXML private VBox displayCardCompanyOffer;
    @FXML private Label usernameLabel;

    @FXML
    public void initialize() {
        loadCompanyCards();
    }

    private void loadCompanyCards() {

        String sql = "SELECT id, company_name, description, logo_path FROM companies";
        List<Node> cards = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int companyId = rs.getInt("id");
                String name = rs.getString("company_name");
                String description = rs.getString("description");
                String logoPath = rs.getString("logo_path");

                Image logo = null;
                if (logoPath != null && new File(logoPath).exists()) {
                    logo = new Image(new File(logoPath).toURI().toString());
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCard.fxml"));
                Node card = loader.load();

                CompanyCardController controller = loader.getController();
                controller.setCompanyData(name, logo);

                controller.getDeleteButton().setVisible(false);
                controller.getDeleteButton().setManaged(false);

                card.setOnMouseClicked(e -> openCompanyInfoPage(companyId, name, logoPath, description));

                cards.add(card);
            }

            for (int i = 0; i < cards.size(); i += 4) {

                HBox row = new HBox();
                row.setSpacing(40);
                row.setAlignment(Pos.CENTER_LEFT);

                for (int j = 0; j < 4 && (i + j) < cards.size(); j++) {
                    row.getChildren().add(cards.get(i + j));
                }

                displayCardCompanyOffer.getChildren().add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCompanyInfoPage(int companyId, String name, String logoPath, String description) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userDashboardFillInformation.fxml"));
            Parent root = loader.load();

            CompanyInformationController controller = loader.getController();
            controller.setCompanyData(companyId, name, logoPath, description);
            controller.setCompanyId(companyId);
            controller.setUsername(currentUserEmail); // keep this ONLY for display



            Stage stage = (Stage) displayCardCompanyOffer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int companyId;

    public void setCompanyId(int id) { this.companyId = id; }

    @FXML
    private void onSignOutClick(MouseEvent event) throws Exception {
        loadScene(event, "login.fxml");
    }

    @FXML
    private void onUserInboxClick(MouseEvent event) throws Exception {
        loadScene(event, "userInboxDashboard.fxml");
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

    private void loadScene(MouseEvent event, String fxml) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private String currentUserEmail;

    public void setUsername(String username) {
        this.currentUserEmail = username;
        usernameLabel.setText("@" + username);
    }
}
