
        package com.example.practice2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class CompanyInformationController {

    @FXML private ChoiceBox<String> onBranchOption;
    @FXML private ChoiceBox<String> onLocation;
    @FXML private ChoiceBox<String> positionChoiceBox;

    // --- Company state ---
    private int companyId;
    private String companyName;
    private String companyLogoPath;
    public void loadApplicantData(int applicantId) {
        System.out.println("Applicant ID received: " + applicantId);
    }


    // --- Top area (from FXML) ---
    @FXML private Label companyNameLabel;
    @FXML private TextFlow descriptionLabel;
    @FXML private ImageView companyLogoImageView;

    // --- Form fields (match your FXML layout) ---
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField middleInitialField;

    @FXML private TextField dobField;      // date of birth (plain text in your FXML)
    @FXML private TextField ageField;
    @FXML private CheckBox maleCheckBox;
    @FXML private CheckBox femaleCheckBox;

    @FXML private TextField contactField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    @FXML private TextField nationalityField;
    @FXML private TextField occupationField;
    @FXML private TextField religionField;



    // Attach file buttons (your FXML has Attach File buttons)
    @FXML private Button attachPictureButton;
    @FXML private Button attachResumeButton;
    @FXML private Button attachPhilButton;

    // Submit button
    @FXML private Button submitButton;

    // Internal holders for selected file paths
    private String selectedPicturePath = null;
    private String selectedResumePath = null;
    private String selectedPhilPath = null;

    // Basic email regex for lightweight validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

    @FXML
    public void initialize() {
        // Ensure gender boxes are mutually exclusive
        maleCheckBox.selectedProperty().addListener((obs, was, isNow) -> {
            if (isNow) femaleCheckBox.setSelected(false);
        });
        femaleCheckBox.selectedProperty().addListener((obs, was, isNow) -> {
            if (isNow) maleCheckBox.setSelected(false);
        });

        // Hook attach buttons (in case you prefer to set handlers in code)
        attachPictureButton.setOnMouseClicked(this::onAttachPictureClick);
        attachResumeButton.setOnMouseClicked(this::onAttachResumeClick);
        attachPhilButton.setOnMouseClicked(this::onAttachPhilClick);

        // Hook submit (in case FXML did not already map onMouseClicked)
        submitButton.setOnMouseClicked(this::onSubmitClick);

//        try {
//            onBranchOption.getItems().setAll(BranchDAO.getBranches(companyId));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        onBranchOption.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            loadLocationOptions();
        });

        onLocation.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            loadPositionOptions();
        });
    }
    private void loadLocationOptions() {
        try {
            String branch = onBranchOption.getValue();
            int branchId = BranchDAO.getBranchId(branch, companyId);

            onLocation.getItems().setAll(LocationDAO.getLocations(companyId, branchId));
        } catch(Exception e) {}
    }

    private void loadPositionOptions() {
        try {
            String branch = onBranchOption.getValue();
            String location = onLocation.getValue();

            int branchId = BranchDAO.getBranchId(branch, companyId);
            int locId = LocationDAO.getLocationId(location, branchId, companyId);

            positionChoiceBox.getItems().setAll(PositionDAO.getPositions(companyId, branchId, locId));
        } catch(Exception e) {}
    }
    /**
     * Called by the calling controller (UserDashboardController) after loading this FXML.
     * Use this to pass the company id so we can save applicants under the correct company.
     */
    public void setCompanyData(int id, String companyName, String logoPath, String description) {
        this.companyId = id;
        this.companyName = companyName;
        this.companyLogoPath = logoPath;


        companyNameLabel.setText(companyName);

        descriptionLabel.getChildren().clear();
        descriptionLabel.getChildren().add(new Text(description == null ? "" : description));

        if (logoPath != null && new File(logoPath).exists()) {
            companyLogoImageView.setImage(new Image(new File(logoPath).toURI().toString()));
        }

        try {
            onBranchOption.getItems().setAll(BranchDAO.getBranches(companyId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Populate positions for this company (if any)
        loadPositionsForCompany();
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }




    // --- Load positions to ChoiceBox from positions table (company-specific) ---
    private void loadPositionsForCompany() {
        positionChoiceBox.getItems().clear();

        String sql = "SELECT position_name FROM positions WHERE company_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                positionChoiceBox.getItems().add(rs.getString("position_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Optionally set a placeholder value if none loaded
        

    }

    // --- Attach file handlers ---
    private void onAttachPictureClick(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose 2x2 picture");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = chooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            selectedPicturePath = file.getAbsolutePath();
            attachPictureButton.setText("Selected");
        }
    }

    private void onAttachResumeClick(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose resume (PDF or DOC)");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.doc", "*.docx")
        );
        File file = chooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            selectedResumePath = file.getAbsolutePath();
            attachResumeButton.setText("Selected");
        }
    }

    private void onAttachPhilClick(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose PhilHealth (or other) file");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.jpg", "*.png", "*.docx")
        );
        File file = chooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            selectedPhilPath = file.getAbsolutePath();
            attachPhilButton.setText("Selected");
        }
    }

    // --- Submit handler (wired in FXML with onMouseClicked="#onSubmitClick") ---
    @FXML
    private void onSubmitClick(MouseEvent event) {

        if (!validateForm()) return;

        boolean saved = saveApplicantToDatabase();
        if (!saved) {
            new Alert(Alert.AlertType.ERROR, "Failed to save application.").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Application Submitted");
        alert.setHeaderText("Success!");
        alert.setContentText("Your application has been submitted successfully.");
        alert.showAndWait();

        // Load the user's profile and pass the email so it displays newly-saved data
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userProfileDashboard.fxml"));
            Parent root = loader.load();

            UserProfileController controller = loader.getController();
            controller.setUsername(currentUsername);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Label usernameLabel;

    // FIX: currentUsername must exist because it is used in many places.
    private String currentUserEmail;
    private String currentUsername;


    public void setUsername(String username) {
        this.currentUserEmail = username;
        usernameLabel.setText("@" + username);
    }






    // --- Minimal validation ---
    private boolean validateForm() {
        String fn = firstNameField.getText();
        String ln = lastNameField.getText();
        String email = emailField.getText();
        String ageText = ageField.getText();

        if (fn == null || fn.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "First name is required.").show();
            return false;
        }
        if (ln == null || ln.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Last name is required.").show();
            return false;
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid email address.").show();
            return false;
        }
        if (ageText != null && !ageText.trim().isEmpty()) {
            try {
                int age = Integer.parseInt(ageText.trim());
                if (age < 0 || age > 150) {
                    new Alert(Alert.AlertType.WARNING, "Please enter a realistic age.").show();
                    return false;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Age must be a number.").show();
                return false;
            }
        }
        return true;
    }

    // --- Save to DB: returns true on success ---
    private boolean saveApplicantToDatabase() {

        String insertApplicantSQL =
                "INSERT INTO applicants (company_id, first_name, last_name, middle_initial, date_of_birth, age, gender, contact, " +
                        "email, address, nationality, occupation, religion, picture_path, resume_path, philhealth_path, " +
                        "position, branch, location) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertRecentSQL =
                "INSERT INTO recently_applied (applicant_id, name, company, date, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect()) {

            // Insert applicant + RETURN GENERATED ID
            PreparedStatement applicantStmt =
                    conn.prepareStatement(insertApplicantSQL, PreparedStatement.RETURN_GENERATED_KEYS);

            applicantStmt.setInt(1, companyId);
            applicantStmt.setString(2, firstNameField.getText().trim());
            applicantStmt.setString(3, lastNameField.getText().trim());
            applicantStmt.setString(4, middleInitialField.getText().trim());
            applicantStmt.setString(5, dobField.getText().trim());

            String ageText = ageField.getText().trim();
            if (ageText.isEmpty()) applicantStmt.setNull(6, java.sql.Types.INTEGER);
            else applicantStmt.setInt(6, Integer.parseInt(ageText));

            String gender = maleCheckBox.isSelected() ? "Male" :
                    femaleCheckBox.isSelected() ? "Female" : "";
            applicantStmt.setString(7, gender);

            applicantStmt.setString(8, contactField.getText().trim());
            applicantStmt.setString(9, emailField.getText().trim());
            applicantStmt.setString(10, addressField.getText().trim());
            applicantStmt.setString(11, nationalityField.getText().trim());
            applicantStmt.setString(12, occupationField.getText().trim());
            applicantStmt.setString(13, religionField.getText().trim());
            applicantStmt.setString(14, selectedPicturePath);
            applicantStmt.setString(15, selectedResumePath);
            applicantStmt.setString(16, selectedPhilPath);

            applicantStmt.setString(17, positionChoiceBox.getValue());
            applicantStmt.setString(18, onBranchOption.getValue());
            applicantStmt.setString(19, onLocation.getValue());

            applicantStmt.executeUpdate();

            // Get applicant ID
            ResultSet rs = applicantStmt.getGeneratedKeys();
            int applicantId = 0;
            if (rs.next()) {
                applicantId = rs.getInt(1);
            }

            // Insert into recently_applied
            PreparedStatement recentStmt = conn.prepareStatement(insertRecentSQL);

            String fullName = firstNameField.getText().trim() + " " + lastNameField.getText().trim();
            String todayDate = java.time.LocalDate.now().toString();

            recentStmt.setInt(1, applicantId);
            recentStmt.setString(2, fullName);
            recentStmt.setString(3, companyName); // you already have this field
            recentStmt.setString(4, todayDate);
            recentStmt.setString(5, "new");

            recentStmt.executeUpdate();

            System.out.println("✔ Applicant inserted with ID: " + applicantId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }





    private void addToRecentApplications(String name, String company, String date, String status) {
        String sql = "INSERT INTO recently_applied (name, company, date, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, company);
            stmt.setString(3, date);
            stmt.setString(4, status);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getLoggedInUserId(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }




    @FXML
    private void onUserDashboardClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userDashboard.fxml"));
        Parent root = loader.load();

        UserDashboardController controller = loader.getController();
        controller.setUsername(currentUserEmail);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void onUserProfileClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userProfileDashboard.fxml"));
        Parent root = loader.load();

        UserProfileController controller = loader.getController(); // ✅ FIXED
        controller.setUsername(currentUserEmail);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



    @FXML
    private void onUserInboxClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userInboxDashboard.fxml"));
        Parent root = loader.load();

        UserInboxDashboardController controller = loader.getController();
        controller.setUsername(currentUserEmail);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void onSignOutClick(MouseEvent event) throws Exception {
        loadScene(event, "login.fxml");
    }



    // Small scene loader reused
    private void loadScene(MouseEvent event, String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        // attempt to pass the current user email to controllers that support it
        Object controller = loader.getController();
        try {
            if (controller instanceof UserProfileController) {
                ((UserProfileController) controller).setUsername(currentUsername);
            } else if (controller instanceof UserDashboardController) {
                ((UserDashboardController) controller).setUsername(currentUsername);
            } else if (controller instanceof UserInboxDashboardController) {
                ((UserInboxDashboardController) controller).setUsername(currentUsername);
            }
        } catch (Exception e) {
            // fail silently if controller doesn't have the setter
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}