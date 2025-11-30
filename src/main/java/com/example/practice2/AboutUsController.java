package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutUsController {

    private String rawUsername;

    public void setAdminUsername(String username) {
        this.rawUsername = username;
    }



    // ----------------- SIGN OUT -----------------
    @FXML
    private void onsignOutButton(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    @FXML
    private void onBinButtonClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bin.fxml"));
        Parent root = loader.load();
        BinController controller = loader.getController();
        controller.setAdminUsername(rawUsername);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void onDashboardButtonClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_dashboard.fxml"));
        Parent root = loader.load();

        AdminController controller = loader.getController();
        controller.setAdminUsername(rawUsername);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
