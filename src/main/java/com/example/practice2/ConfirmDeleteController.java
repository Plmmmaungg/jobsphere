package com.example.practice2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmDeleteController {

    @FXML
    private Button cancelButton, confirmButton;

    private Runnable onConfirmAction;

    public void setOnConfirm(Runnable action) {
        this.onConfirmAction = action;
    }

    @FXML
    private void initialize() {
        // Close popup on cancel
        cancelButton.setOnAction(e -> close());

        // Confirm deletion and then close popup
        confirmButton.setOnAction(e -> {
            if (onConfirmAction != null) {
                try {
                    onConfirmAction.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            close(); // ✅ ensure popup closes after action
        });
    }

    private void close() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
