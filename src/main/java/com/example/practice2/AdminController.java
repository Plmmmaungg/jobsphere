package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminController {

    @FXML private TableView<RecentApplication> recentlyApply;
    @FXML private TableColumn<RecentApplication, String> viewName;
    @FXML private TableColumn<RecentApplication, String> viewCompany;
    @FXML private TableColumn<RecentApplication, String> viewDate;
    @FXML private TableColumn<RecentApplication, String> viewStatus;
    private String companyName;
    private Runnable onLoginSuccess;

    public void setCompanyName(String name) {
        this.companyName = name;
    }

    public void setOnLoginSuccess(Runnable action) {
        this.onLoginSuccess = action;
    }

    @FXML
    private AnchorPane addCompanyForm;

    @FXML
    private Button addButton;

    @FXML
    private TextField companyNameField, usernameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView logoPreview;

    @FXML
    private HBox displayCardCompany;

    @FXML
    private AnchorPane securityFormContainer;

    private Image uploadedLogo;
    private File logoFile;

    @FXML
    private Label usernameLabel;

    private String rawUsername;

    public void setAdminUsername(String username) {
        this.rawUsername = username;
        usernameLabel.setText("@" + username);
    }







    // ✅ "Add" button
    @FXML
    private void onAddButtonClick() {
        addCompanyForm.setVisible(true);
        addCompanyForm.setManaged(true);
        displayCardCompany.setVisible(true);
        displayCardCompany.setManaged(true);
    }

    // ✅ "Back" button
    @FXML
    private void onFormBackButtonClick() {
        addCompanyForm.setVisible(false);
        addCompanyForm.setManaged(false);
        displayCardCompany.setVisible(true);
        displayCardCompany.setManaged(true);
    }

    // ✅ "Attach Logo" button
    @FXML
    private void onAttachLogoClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        logoFile = fileChooser.showOpenDialog(null);

        if (logoFile != null) {
            uploadedLogo = new Image(logoFile.toURI().toString());
            logoPreview.setImage(uploadedLogo);
        }
    }

    // ✅ "Save" button — insert company + create UI card + open SecurityForm
    @FXML
    private void onSaveButtonClick() {
        String name = companyNameField.getText().trim();
        String desc = descriptionField.getText().trim();
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();
        Image logo = uploadedLogo;

        if (name.isEmpty() || user.isEmpty() || pass.isEmpty() || logo == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields and upload a logo.").showAndWait();
            return;
        }

        int companyId = -1;

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO companies (company_name, description, username, password, logo_path) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setString(3, user);
            stmt.setString(4, pass);
            stmt.setString(5, logoFile.getAbsolutePath());
            stmt.executeUpdate();

            var rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                companyId = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final int finalCompanyId = companyId;
        final String finalName = name;
        final String finalUser = user;
        final String finalPass = pass;
        final Image finalLogo = logo;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCard.fxml"));
            Node card = loader.load();

            CompanyCardController controller = loader.getController();
            controller.setCompanyId(finalCompanyId);
            controller.setCompanyData(finalName, finalLogo);
            controller.setParentContainer(displayCardCompany);
            controller.setRootNode(card);

            controller.setOnCardClicked(() ->
                    openSecurityForm(finalCompanyId, finalName, finalUser, finalPass, finalLogo)
            );

            displayCardCompany.getChildren().add(card);

        } catch (IOException e) {
            e.printStackTrace();
        }

        addCompanyForm.setVisible(false);
        addCompanyForm.setManaged(false);

        displayCardCompany.setVisible(true);
        displayCardCompany.setManaged(true);

        new Alert(Alert.AlertType.INFORMATION, "Company Saved Successfully!").showAndWait();

    }



    // ✅ Show SecurityForm.fxml inside admin dashboard



    private void openSecurityForm(int companyId, String companyName, String username, String password, Image logo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("securityForm.fxml"));
            AnchorPane securityForm = loader.load();

            SecurityFormController controller = loader.getController();
            controller.setCompanyData(companyId, companyName, username, password, logo);
            controller.setParentController(this);

            controller.setOnLoginSuccess(() -> {
                displayCardCompany.setVisible(false);
                displayCardCompany.setManaged(false);
            });



            // show only security form
            securityFormContainer.getChildren().setAll(securityForm);
            securityFormContainer.setVisible(true);
            securityFormContainer.setManaged(true);

            // hide others
            addCompanyForm.setVisible(false);
            addCompanyForm.setManaged(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    // ✅ Called from SecurityFormController when clicking Back
    public void showDashboardView() {
        securityFormContainer.setVisible(false);
        securityFormContainer.setManaged(false);

        displayCardCompany.setVisible(true);
        displayCardCompany.setManaged(true);
    }


    // ✅ Automatically load existing companies from DB
    @FXML
    public void initialize() {
        recentlyApply.setRowFactory(tv -> {
            TableRow<RecentApplication> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    RecentApplication selected = row.getItem();
                    openCompanyLoginFromRow(selected, row);
                }
            });


            return row;
        });

        loadCompaniesFromDatabase();
        loadRecentApplications();


    }

    private void openCompanyLoginFromRow(RecentApplication selected, TableRow<RecentApplication> row) {
        String companyName = selected.getCompany();

        try (Connection conn = DatabaseConnection.connect()) {

            String sql = "SELECT id, company_name, username, password, logo_path FROM companies WHERE company_name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, companyName);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                int companyId = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                String logoPath = rs.getString("logo_path");
                Image logo = (logoPath != null && new File(logoPath).exists())
                        ? new Image(new File(logoPath).toURI().toString())
                        : null;

                // open existing Security Form
                openSecurityForm(companyId, companyName, username, password, logo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void loadRecentApplications() {
        String sql = "SELECT name, company, date, status FROM recently_applied";

        viewName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        viewCompany.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCompany()));
        viewDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
        viewStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        recentlyApply.getItems().clear();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                recentlyApply.getItems().add(new RecentApplication(
                        rs.getString("name"),
                        rs.getString("company"),
                        rs.getString("date"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class RecentApplication {
        private String name;
        private String company;
        private String date;
        private String status;

        public RecentApplication(String name, String company, String date, String status) {
            this.name = name;
            this.company = company;
            this.date = date;
            this.status = status;
        }

        public String getName() { return name; }
        public String getCompany() { return company; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }


    private void loadCompaniesFromDatabase() {
        String sql = "SELECT id, company_name, username, password, logo_path FROM companies";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {

                final int companyId = rs.getInt("id");
                final String name = rs.getString("company_name");
                final String user = rs.getString("username");
                final String pass = rs.getString("password");

                String logoPath = rs.getString("logo_path");
                final Image logo = (logoPath != null && new File(logoPath).exists())
                        ? new Image(new File(logoPath).toURI().toString())
                        : null;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("companyCard.fxml"));
                Node card = loader.load();

                CompanyCardController controller = loader.getController();
                controller.setCompanyId(companyId);
                controller.setCompanyData(name, logo);
                controller.setParentContainer(displayCardCompany);
                controller.setRootNode(card);

                controller.setOnCardClicked(() ->
                        openSecurityForm(companyId, name, user, pass, logo)
                );

                displayCardCompany.getChildren().add(card);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Logout
    @FXML
    private void onBackButtonClick(MouseEvent event) throws IOException {
        Parent registerRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
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
        Parent root = FXMLLoader.load(getClass().getResource("admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}
