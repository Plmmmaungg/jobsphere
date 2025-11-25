package com.example.practice2.testing;

import com.example.practice2.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserPositionController {

    @FXML
    private ChoiceBox<String> positionChoiceBox;

    @FXML
    private Button saveButton;

    @FXML
    private Text selectedPositionText;

    @FXML
    public void initialize() {
        loadPositions();
    }

    private void loadPositions() {
        try (Connection conn = DatabaseConnection.connect()) {

            String sql = "SELECT position_name FROM positions";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                positionChoiceBox.getItems().add(rs.getString("position_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveClick() {
        String selected = positionChoiceBox.getValue();
        if (selected != null) {
            selectedPositionText.setText(selected);
        }
    }

    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
