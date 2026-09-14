package org.example.oop2_finalproject.views.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.oop2_finalproject.Database_Service.CashiersRepository;
import org.example.oop2_finalproject.models.Cashier;

import java.util.List;

public class ManageCashiersView extends BorderPane {

    private final CashiersRepository repo = new CashiersRepository();

    // FORM FIELDS
    private TextField usernameField, searchField;
    private PasswordField passwordField;

    // TABLE
    private TableView<Cashier> table;
    private ObservableList<Cashier> displayedCashiers;

    // PAGINATION
    private final int ITEMS_PER_PAGE = 10;
    private int currentPage = 1;
    private int totalPages = 1;

    public ManageCashiersView() {
        buildUI();
        loadAllCashiers();
    }

    // ===========================================================
    // BUILD UI
    // ===========================================================
    private void buildUI() {

        // ==========================
        // TOP — SEARCH BAR
        // ==========================
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search cashier by username...");

        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> searchCashiers());

        searchBox.getChildren().addAll(searchField, searchBtn);
        topBox.getChildren().add(searchBox);

        // ==========================
        // CENTER — TABLE
        // ==========================
        table = new TableView<>();

        TableColumn<Cashier, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Cashier, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(c -> c.getValue().usernameProperty());
        colUsername.setPrefWidth(200);

        TableColumn<Cashier, String> colPassword = new TableColumn<>("Password");
        colPassword.setCellValueFactory(c -> c.getValue().passwordProperty());
        colPassword.setPrefWidth(200);

        table.getColumns().addAll(colId, colUsername, colPassword);

        // Auto-fill form on row select
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

        addBtn.setOnAction(e -> addCashier());
        updateBtn.setOnAction(e -> updateCashier());
        deleteBtn.setOnAction(e -> deleteCashier());
        refreshBtn.setOnAction(e -> {
            searchField.clear();
            loadAllCashiers();
        });

        btnBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, refreshBtn);

        // PAGINATION
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
    private void loadAllCashiers() {
        List<Cashier> list = repo.getAllCashiers();
        displayedCashiers = FXCollections.observableArrayList(list);
        setupPagination();
    }

    private void searchCashiers() {
        String keyword = searchField.getText().trim();
        displayedCashiers = FXCollections.observableArrayList(repo.searchCashiers(keyword));
        setupPagination();
    }

    private void setupPagination() {
        int totalItems = displayedCashiers.size();
        totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        currentPage = 1;
        updateTablePage();
    }

    private void updateTablePage() {
        int from = (currentPage - 1) * ITEMS_PER_PAGE;
        int to = Math.min(from + ITEMS_PER_PAGE, displayedCashiers.size());

        table.setItems(FXCollections.observableArrayList(
                displayedCashiers.subList(from, to)
        ));
    }

    // ===========================================================
    // CRUD OPERATIONS
    // ===========================================================
    private void addCashier() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Fill all fields.");
            return;
        }

        if (repo.addCashier(username, password)) {
            showAlert("Cashier added successfully!");
            clearForm();
            loadAllCashiers();
        } else {
            showAlert("Error adding cashier.");
        }
    }

    private void updateCashier() {
        Cashier selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a cashier to update.");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Fill all fields.");
            return;
        }

        if (repo.updateCashier(selected.getId(), username, password)) {
            showAlert("Cashier updated!");
            clearForm();
            loadAllCashiers();
        } else {
            showAlert("Error updating cashier.");
        }
    }

    private void deleteCashier() {
        Cashier selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a cashier to delete.");
            return;
        }

        if (repo.deleteCashier(selected.getId())) {
            showAlert("Cashier deleted!");
            clearForm();
            loadAllCashiers();
        } else {
            showAlert("Error deleting cashier.");
        }
    }

    // ===========================================================
    // HELPERS
    // ===========================================================
    private void fillForm(Cashier c) {
        usernameField.setText(c.getUsername());
        passwordField.setText(c.getPassword());
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
