package org.example.oop2_finalproject.views.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.oop2_finalproject.Database_Service.AdminsRepository;
import org.example.oop2_finalproject.models.Admin;

import java.util.List;

public class ManageAdminsView extends BorderPane {

    private final AdminsRepository repo = new AdminsRepository();

    private TextField usernameField, searchField;
    private PasswordField passwordField;

    private TableView<Admin> table;
    private ObservableList<Admin> displayedAdmins;

    private final int ITEMS_PER_PAGE = 10;
    private int currentPage = 1;
    private int totalPages = 1;

    public ManageAdminsView() {
        buildUI();
        loadAllAdmins();
    }


    private void buildUI() {

        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search admin by username...");   // hay mnaamela la ybayyin searchadminbyusername b aleb l searchfield

        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> searchAdmins());

        searchBox.getChildren().addAll(searchField, searchBtn);
        topBox.getChildren().add(searchBox);

        // ==========================
        // CENTER — TABLE
        // ==========================
        table = new TableView<>();

        TableColumn<Admin, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(a -> a.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Admin, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(a -> a.getValue().usernameProperty());
        colUsername.setPrefWidth(200);

        TableColumn<Admin, String> colPassword = new TableColumn<>("Password");
        colPassword.setCellValueFactory(a -> a.getValue().passwordProperty());
        colPassword.setPrefWidth(200);

        table.getColumns().addAll(colId, colUsername, colPassword);

        // Fill form when selecting a row
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) fillForm(selected);
        });

        // ==========================
        // BOTTOM — FORM + BUTTONS + PAGINATION
        // ==========================
        VBox bottomBox = new VBox(12);
        bottomBox.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        usernameField = new TextField();
        passwordField = new PasswordField();

        form.add(new Label("Username:"), 0, 0);
        form.add(usernameField, 1, 0);

        form.add(new Label("Password:"), 0, 1);
        form.add(passwordField, 1, 1);

        // CRUD Buttons
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        addBtn.setOnAction(e -> addAdmin());
        updateBtn.setOnAction(e -> updateAdmin());
        deleteBtn.setOnAction(e -> deleteAdmin());
        refreshBtn.setOnAction(e -> {
            searchField.clear();
            loadAllAdmins();
        });

        btnBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, refreshBtn);

        // PAGINATION CONTROLS
        HBox pagination = new HBox(10);
        pagination.setAlignment(Pos.CENTER);

        Button prev = new Button("<< Previous");
        Button next = new Button("Next >>");
        Label pageLabel = new Label();

        prev.setOnAction(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateTablePage();
            }
        });

        next.setOnAction(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateTablePage();
            }
        });

        pagination.getChildren().addAll(prev, pageLabel, next);

        bottomBox.getChildren().addAll(form, btnBox, pagination);

        // ADD ALL TO LAYOUT
        this.setTop(topBox);
        this.setCenter(table);
        this.setBottom(bottomBox);
    }

    // ===========================================================
    // LOADING & PAGINATION
    // ===========================================================
    private void loadAllAdmins() {
        List<Admin> list = repo.getAllAdmins();
        displayedAdmins = FXCollections.observableArrayList(list);
        setupPagination();
    }

    private void searchAdmins() {
        String keyword = searchField.getText().trim();
        displayedAdmins = FXCollections.observableArrayList(repo.searchAdmins(keyword));
        setupPagination();
    }

    private void setupPagination() {
        int totalItems = displayedAdmins.size();
        totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        currentPage = 1;
        updateTablePage();
    }

    private void updateTablePage() {
        int from = (currentPage - 1) * ITEMS_PER_PAGE;
        int to = Math.min(from + ITEMS_PER_PAGE, displayedAdmins.size());

        table.setItems(FXCollections.observableArrayList(
                displayedAdmins.subList(from, to)
        ));
    }

    // ===========================================================
    // CRUD OPERATIONS
    // ===========================================================
    private void addAdmin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Fill all fields.");
            return;
        }

        if (repo.addAdmin(username, password)) {
            showAlert("Admin added successfully!");
            clearForm();
            loadAllAdmins();
        } else {
            showAlert("Error adding admin.");
        }
    }

    private void updateAdmin() {
        Admin selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an admin to update.");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Fill all fields.");
            return;
        }

        if (repo.updateAdmin(selected.getId(), username, password)) {
            showAlert("Admin updated successfully!");
            clearForm();
            loadAllAdmins();
        } else {
            showAlert("Error updating admin.");
        }
    }

    private void deleteAdmin() {
        Admin selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an admin to delete.");
            return;
        }

        if (repo.deleteAdmin(selected.getId())) {
            showAlert("Admin deleted successfully!");
            clearForm();
            loadAllAdmins();
        } else {
            showAlert("Error deleting admin.");
        }
    }

    // ===========================================================
    // HELPERS
    // ===========================================================
    private void fillForm(Admin admin) {
        usernameField.setText(admin.getUsername());
        passwordField.setText(admin.getPassword());
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
