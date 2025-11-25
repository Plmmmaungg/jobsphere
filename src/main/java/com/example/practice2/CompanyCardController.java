package com.example.practice2;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CompanyCardController {

    @FXML
    private VBox cardBox;

    @FXML
    private ImageView logoImage;

    @FXML
    private Button deleteButton;

    private HBox parentContainer;
    private Node rootNode;           // ✅ NEW: reference to the actual card node
    private String companyName;


    private int companyId;

    public void setCompanyId(int id) {
        this.companyId = id;
    }
    public int getCompanyId() {
        return companyId;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    @FXML
    private void onCardHover() {
        cardBox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 4); -fx-cursor: hand;");
        cardBox.setScaleX(1.02);
        cardBox.setScaleY(1.02);
    }

    @FXML
    private void onCardExit() {
        cardBox.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-cursor: hand;");
        cardBox.setScaleX(1);
        cardBox.setScaleY(1);
    }

    @FXML
    private void onCardPress() {
        cardBox.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0; -fx-border-radius: 20; -fx-background-radius: 20; -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.08), 3, 0, 0, 1); -fx-cursor: hand;");
        cardBox.setScaleX(1.02);
        cardBox.setScaleY(1.02);
    }

    @FXML
    private void onCardRelease() {
        // Restore hover or default state depending on mouse position
        if (cardBox.isHover()) {
            onCardHover();
        } else {
            onCardExit();
        }
    }


    public void setCompanyData(String companyName, Image logo) {
        this.companyName = companyName;
        logoImage.setImage(logo);
    }

    public void setParentContainer(HBox parent) {
        this.parentContainer = parent;
    }

    // ✅ NEW: called from AdminController after loading FXML
    public void setRootNode(Node node) {
        this.rootNode = node;
    }

    @FXML
    private void onDeleteButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmDelete.fxml"));
            Parent root = loader.load();

            ConfirmDeleteController controller = loader.getController();
            controller.setOnConfirm(() -> {
                deleteCompanyFromDatabase();
                removeCardFromUI();
                showToast("✅ Company deleted successfully!");
            });

            Stage popupStage = new Stage();
            popupStage.initOwner(deleteButton.getScene().getWindow());
            popupStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            popupStage.setScene(new Scene(root));
            popupStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteCompanyFromDatabase() {
        String sql = "DELETE FROM companies WHERE company_name = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, companyName);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            showToast("❌ Failed to delete company from database!");
        }
    }

    // ✅ Use the rootNode reference to remove the card properly
    private void removeCardFromUI() {
        if (parentContainer != null && rootNode != null) {
            parentContainer.getChildren().remove(rootNode);
        }
    }

    private void showToast(String message) {
        Stage stage = (Stage) logoImage.getScene().getWindow();

        Popup popup = new Popup();
        popup.setAutoHide(true);

        Text text = new Text(message);
        text.setStyle("""
            -fx-background-color: #333333;
            -fx-text-fill: white;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 10;
        """);

        VBox toast = new VBox(text);
        toast.setStyle("-fx-background-color: transparent;");
        popup.getContent().add(toast);

        popup.show(stage);
        popup.setX(stage.getX() + stage.getWidth() - 250);
        popup.setY(stage.getY() + 50);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }
    private Runnable onCardClicked;

    public void setOnCardClicked(Runnable action) {
        this.onCardClicked = action;
    }

    @FXML
    private void initialize() {
        // ✅ Make both the image and entire VBox clickable
        cardBox.setOnMouseClicked(e -> {
            if (onCardClicked != null) onCardClicked.run();
        });

        logoImage.setOnMouseClicked(e -> {
            if (onCardClicked != null) onCardClicked.run();
        });
    }


}