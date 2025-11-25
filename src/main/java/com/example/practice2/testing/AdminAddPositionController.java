package com.example.practice2.testing;

import com.example.practice2.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminAddPositionController {

    @FXML
    private TextField positionTextField;

    @FXML
    private Button saveButton;

    @FXML
    private VBox positionListVBox;

    @FXML
    public void initialize() {
        loadPositions();
    }

    @FXML
    private void onSaveClick() {
        String position = positionTextField.getText().trim();

        if (position.isEmpty()) return;

        try (Connection conn = DatabaseConnection.connect()) {

            String sql = "INSERT INTO positions (position_name) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, position);
            stmt.executeUpdate();

            positionTextField.clear();
            loadPositions();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPositions() {
        positionListVBox.getChildren().clear();

        try (Connection conn = DatabaseConnection.connect()) {

            String sql = "SELECT position_name FROM positions";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Label lbl = new Label(rs.getString("position_name"));
                lbl.setStyle("-fx-font-size: 16px;");
                positionListVBox.getChildren().add(lbl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
