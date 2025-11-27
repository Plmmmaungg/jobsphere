package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.*;


import java.io.IOException;


public class PositionController {

    @FXML private TextField onBranchTextField;
    @FXML private TextField onLocationTextField;
    @FXML private TextField onPositionTextField;

    @FXML private ListView<String> viewBranch;
    @FXML private ListView<String> viewLocation;
    @FXML private ListView<String> viewPosition;

    @FXML private Button addBranchAddress;
    @FXML private Button addPosition;

    @FXML
    public void initialize() {

        viewBranch.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) loadLocations();
        });

        viewLocation.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) loadPositions();
        });
    }

    private void loadBranches() {
        try {
            viewBranch.getItems().setAll(BranchDAO.getBranches(companyId));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadLocations() {
        try {
            String branch = viewBranch.getSelectionModel().getSelectedItem();
            if (branch == null) return;

            int branchId = BranchDAO.getBranchId(branch, companyId);
            viewLocation.getItems().setAll(LocationDAO.getLocations(companyId, branchId));

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadPositions() {
        try {
            String branch = viewBranch.getSelectionModel().getSelectedItem();
            String location = viewLocation.getSelectionModel().getSelectedItem();

            if (branch == null || location == null) return;

            int branchId = BranchDAO.getBranchId(branch, companyId);
            int locationId = LocationDAO.getLocationId(location, branchId, companyId);

            viewPosition.getItems().setAll(PositionDAO.getPositions(companyId, branchId, locationId));

        } catch (Exception e) { e.printStackTrace(); }
    }

    private int companyId;

    public void setCompanyId(int id) {
        this.companyId = id;
        loadBranches(); // reload branches specific to this company
    }

    @FXML
    private void addBranch() {
        try {
            BranchDAO.addBranch(onBranchTextField.getText(), companyId);
            loadBranches();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void addLocation() {
        try {
            String branch = viewBranch.getSelectionModel().getSelectedItem();
            if (branch == null) return;

            int branchId = BranchDAO.getBranchId(branch, companyId);

            LocationDAO.addLocation(onLocationTextField.getText(), branchId, companyId);
            loadLocations();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void addPosition() {
        try {
            String branch = viewBranch.getSelectionModel().getSelectedItem();
            String location = viewLocation.getSelectionModel().getSelectedItem();
            if (branch == null || location == null) return;

            int branchId = BranchDAO.getBranchId(branch, companyId);
            int locationId = LocationDAO.getLocationId(location, branchId, companyId);

            PositionDAO.addPosition(onPositionTextField.getText(), branchId, locationId, companyId);

            loadPositions();
        } catch (Exception e) { e.printStackTrace(); }
    }


    @FXML
    private void onsignOutButton(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    @FXML
    private void onDashboardclick(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCreatedDashboard.fxml"));
        Parent root = loader.load();

        CreatedAdminCompanyController controller = loader.getController();
        controller.setCompanyData(companyId, null, null); // send only companyId

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onAboutUs(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("companyCreatedAboutUs.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }

    @FXML
    private void onPositionClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("companyCreatedPosition.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(registerRoot));
        stage.show();
    }



}
