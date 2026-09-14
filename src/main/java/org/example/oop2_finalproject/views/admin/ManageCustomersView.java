package org.example.oop2_finalproject.views.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.oop2_finalproject.Database_Service.CustomersRepository;
import org.example.oop2_finalproject.models.Customer;

import java.util.List;

public class ManageCustomersView extends BorderPane {

    private final CustomersRepository repo = new CustomersRepository();

    // FORM INPUTS
    private TextField nameField, phoneField, pointsField, searchField;

    // TABLE
    private TableView<Customer> table;
    private ObservableList<Customer> displayedCustomers;

    // PAGINATION
    private final int ITEMS_PER_PAGE = 10;
    private int currentPage = 1;
    private int totalPages = 1;

    public ManageCustomersView() {
        buildUI();
        loadAllCustomers();
    }

    // ==============================================================
    // UI BUILD
    // ==============================================================
    private void buildUI() {

        // ==========================================================
        // TOP: SEARCH BAR
        // ==========================================================
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search name or phone...");
        Button searchBtn = new Button("Search");

        searchBtn.setOnAction(e -> searchCustomers());

        searchBox.getChildren().addAll(searchField, searchBtn);
        topBox.getChildren().add(searchBox);

        // ==========================================================
        // CENTER: TABLE
        // ==========================================================
        table = new TableView<>();

        TableColumn<Customer, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Customer, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colName.setPrefWidth(150);

        TableColumn<Customer, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(c -> c.getValue().phoneProperty());
        colPhone.setPrefWidth(150);

        TableColumn<Customer, Number> colPoints = new TableColumn<>("Points");
        colPoints.setCellValueFactory(c -> c.getValue().pointsProperty());
        colPoints.setPrefWidth(80);

        table.getColumns().addAll(colId, colName, colPhone, colPoints);

        // Fill form when selecting row
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) fillForm(selected);
        });

        // ==========================================================
        // BOTTOM: FORM + CRUD + PAGINATION
        // ==========================================================
        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        nameField = new TextField();
        phoneField = new TextField();
        pointsField = new TextField();

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(new Label("Phone:"), 0, 1);
        form.add(phoneField, 1, 1);

        form.add(new Label("Points:"), 0, 2);
        form.add(pointsField, 1, 2);

        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        addBtn.setOnAction(e -> addCustomer());
        updateBtn.setOnAction(e -> updateCustomer());
        deleteBtn.setOnAction(e -> deleteCustomer());
        refreshBtn.setOnAction(e -> {
            searchField.clear();
            loadAllCustomers();
        });

        btnBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, refreshBtn);

        // ===== PAGINATION =====
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

        // ADD ALL
        this.setTop(topBox);
        this.setCenter(table);
        this.setBottom(bottomBox);
    }

    // ==============================================================
    // LOAD & PAGINATION
    // ==============================================================
    private void loadAllCustomers() {
        List<Customer> list = repo.getAllCustomers();
        displayedCustomers = FXCollections.observableArrayList(list);

        setupPagination();
    }

    private void searchCustomers() {
        String keyword = searchField.getText().trim();
        displayedCustomers = FXCollections.observableArrayList(repo.searchCustomers(keyword));
        setupPagination();
    }

    private void setupPagination() {
        int totalItems = displayedCustomers.size();
        totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        currentPage = 1;
        updateTablePage();
    }

    private void updateTablePage() {
        int from = (currentPage - 1) * ITEMS_PER_PAGE;
        int to = Math.min(from + ITEMS_PER_PAGE, displayedCustomers.size());

        table.setItems(FXCollections.observableArrayList(
                displayedCustomers.subList(from, to)
        ));
    }

    // ==============================================================
    // CRUD LOGIC
    // ==============================================================
    private void addCustomer() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String pointsStr = pointsField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || pointsStr.isEmpty()) {
            showAlert("Fill all fields.");
            return;
        }

        try {
            int points = Integer.parseInt(pointsStr);

            if (repo.addCustomer(name, phone, points)) {
                showAlert("Customer added!");
                clearForm();
                loadAllCustomers();
            } else {
                showAlert("Error adding customer.");
            }

        } catch (NumberFormatException e) {
            showAlert("Points must be a number.");
        }
    }

    private void updateCustomer() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a customer to update.");
            return;
        }

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String pointsStr = pointsField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || pointsStr.isEmpty()) {
            showAlert("Fill all fields.");
            return;
        }

        try {
            int points = Integer.parseInt(pointsStr);

            if (repo.updateCustomer(selected.getId(), name, phone, points)) {
                showAlert("Customer updated!");
                clearForm();
                loadAllCustomers();
            } else {
                showAlert("Error updating customer.");
            }

        } catch (NumberFormatException e) {
            showAlert("Points must be a number.");
        }
    }

    private void deleteCustomer() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a customer to delete.");
            return;
        }

        if (repo.deleteCustomer(selected.getId())) {
            showAlert("Customer deleted!");
            clearForm();
            loadAllCustomers();
        } else {
            showAlert("Error deleting customer.");
        }
    }

    // ==============================================================
    // HELPERS
    // ==============================================================
    private void fillForm(Customer c) {
        nameField.setText(c.getName());
        phoneField.setText(c.getPhone());
        pointsField.setText(String.valueOf(c.getPoints()));
    }

    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        pointsField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
