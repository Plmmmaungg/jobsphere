package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class adminCreatedInboxController {

    @FXML private Label companyNameLabel;
    @FXML private ImageView companyLogoView;

    @FXML private VBox showUserMessageBox;   // LEFT MESSAGE LIST
    @FXML private TextFlow viewMessage;      // RIGHT MESSAGE CONTENT


    @FXML private Text toApplicantName;

    private int companyId;
    private String selectedApplicantName;
    private int selectedApplicantId;



    public void initialize() {
        // nothing yet
    }

    public void setCompanyData(int companyId, String name, Image logo) {
        this.companyId = companyId;
        this.companyLogoView.setImage(logo);
        this.companyNameLabel.setText(name);

        loadMessages();   // <<-- IMPORTANT
    }


    // -----------------------------------------------------------------------
    // LOAD ALL MESSAGES SENT TO THIS COMPANY BY USERS
    // -----------------------------------------------------------------------
    private void loadMessagesForCompany() {
        showUserMessageBox.getChildren().clear();

        String sql = "SELECT id, user_email, message FROM admin_messages WHERE company_id = ? ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int msgId = rs.getInt("id");
                String userEmail = rs.getString("user_email");
                String message = rs.getString("message");

                HBox msgItem = createMessageItem(msgId, userEmail, message);
                showUserMessageBox.getChildren().add(msgItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------------------
    // CREATE CLICKABLE MESSAGE BOX (LEFT SIDE)
    // -----------------------------------------------------------------------
    private HBox createMessageItem(int msgId, String email, String messageText) {
        HBox item = new HBox();
        item.setPadding(new Insets(10));
        item.setSpacing(8);
        item.setPrefWidth(240);
        item.setStyle("-fx-background-color: white; -fx-border-color: #cccccc;");

        Label emailLabel = new Label(email);
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        item.getChildren().add(emailLabel);

        // When admin clicks → show message on right
        item.setOnMouseClicked(e -> showMessage(messageText));

        return item;
    }


    // -----------------------------------------------------------------------
    // NAVIGATION
    // -----------------------------------------------------------------------

    @FXML
    private void onPositionClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedPosition.fxml"));
        Parent root = loader.load();

        PositionController controller = loader.getController();
        controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

        switchToPage(event, root);
    }

    @FXML
    private void onDashboardClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedDashboard.fxml"));
        Parent root = loader.load();

        PositionController controller = loader.getController();
        controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

        switchToPage(event, root);
    }

    @FXML
    private void onInboxClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminCreatedInbox.fxml"));
        Parent root = loader.load();

        adminCreatedInboxController controller = loader.getController();
        controller.setCompanyData(companyId, companyNameLabel.getText(), companyLogoView.getImage());

        switchToPage(event, root);
    }

    @FXML
    private void onsignOutButton(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        switchToPage(event, root);
    }

    private void switchToPage(MouseEvent event, Parent root) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loadMessages() {

        showUserMessageBox.getChildren().clear();

        String sql = """
        SELECT m.message_text, m.applicant_id,
               a.first_name, a.middle_initial, a.last_name
        FROM messages m
        JOIN applicants a ON m.applicant_id = a.id
        WHERE m.company_id = ?
        ORDER BY m.date_sent DESC
    """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int applicantId = rs.getInt("applicant_id");

                String fullName =
                        rs.getString("first_name") + " " +
                                rs.getString("middle_initial") + ". " +
                                rs.getString("last_name");

                String fullMessage = rs.getString("message_text");

                // Load FXML box
                FXMLLoader loader = new FXMLLoader(getClass().getResource("messageBoxAdmin.fxml"));
                AnchorPane box = loader.load();

                MessageBoxAdminController controller = loader.getController();

                controller.setUserName(fullName);

                controller.setOnClickAction(() -> {
                    selectedApplicantName = fullName;
                    selectedApplicantId = applicantId;
                    showMessage(fullMessage);
                });

                showUserMessageBox.getChildren().add(box);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message) {
        viewMessage.getChildren().clear();

        // Update "To: <name>"
        toApplicantName.setText(selectedApplicantName);

        Text text = new Text(message);
        text.setStyle("-fx-font-size: 16;");
        viewMessage.getChildren().add(text);
    }


}
