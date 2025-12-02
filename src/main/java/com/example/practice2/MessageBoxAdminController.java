package com.example.practice2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MessageBoxAdminController {

    @FXML
    private Label adminDisplayUserName;

    @FXML
    private AnchorPane rootPane;  // This is the whole box (clickable)

    private Runnable clickAction;

    public void setUserName(String name) {
        adminDisplayUserName.setText(name);
    }

    public void setOnClickAction(Runnable action) {
        this.clickAction = action;
        rootPane.setOnMouseClicked(e -> clickAction.run());
    }
}
