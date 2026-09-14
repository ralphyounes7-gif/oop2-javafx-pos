package org.example.oop2_finalproject.views.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.oop2_finalproject.Database_Service.CategoriesRepository;
import org.example.oop2_finalproject.models.Category;

public class ManageCategoriesView extends BorderPane {

    private final CategoriesRepository repo = new CategoriesRepository();

    private TableView<Category> table;
    private ObservableList<Category> categoryList;

    private TextField nameField;
    private TextField searchField;

    public ManageCategoriesView() {
        buildUI();
    }

    private void buildUI() {

        // ======================
        // TOP: TITLE + SEARCH
        // ======================
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));

        Label title = new Label("Manage Categories");

        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Search category...");
        Button searchBtn = new Button("Search");

        searchBtn.setOnAction(e -> loadCategories(searchField.getText()));

        searchBox.getChildren().addAll(searchField, searchBtn);

        topSection.getChildren().addAll(title, searchBox);

        // ======================
        // CENTER: TABLEVIEW
        // ======================
        table = new TableView<>();

        TableColumn<Category, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        idCol.setPrefWidth(50);

        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        nameCol.setPrefWidth(200);

        table.getColumns().addAll(idCol, nameCol);

        // Load initial data
        loadCategories("");

        // Select item → put into form
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null)
                nameField.setText(selected.getName());
        });

        // ======================
        // BOTTOM: CRUD FORM
        // ======================
        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(10));
        bottomSection.setAlignment(Pos.CENTER_LEFT);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        Label nameLabel = new Label("Category Name:");
        nameField = new TextField();

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);

        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        buttonBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, refreshBtn);

        // Buttons actions
        addBtn.setOnAction(e -> addCategory());
        updateBtn.setOnAction(e -> updateCategory());
        deleteBtn.setOnAction(e -> deleteCategory());
        refreshBtn.setOnAction(e -> loadCategories(""));

        bottomSection.getChildren().addAll(form, buttonBox);

        // ======================
        // ADD TO BORDERPANE
        // ======================
        this.setTop(topSection);
        this.setCenter(table);
        this.setBottom(bottomSection);
    }

    // =====================================================
    // LOAD CATEGORIES (with optional search)
    // =====================================================
    private void loadCategories(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            categoryList = FXCollections.observableArrayList(repo.getAllCategories());
        } else {
            categoryList = FXCollections.observableArrayList(repo.searchCategories(keyword));
        }

        table.setItems(categoryList);
    }

    // =====================================================
    // ADD CATEGORY
    // =====================================================
    private void addCategory() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            showAlert("Please enter a category name.");
            return;
        }

        if (repo.addCategory(name)) {
            showAlert("Category added!");
            loadCategories("");
            nameField.clear();
        } else {
            showAlert("Error adding category.");
        }
    }

    // =====================================================
    // UPDATE CATEGORY
    // =====================================================
    private void updateCategory() {
        Category selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a category to update.");
            return;
        }

        String newName = nameField.getText();

        if (newName.isEmpty()) {
            showAlert("Please enter a category name.");
            return;
        }

        if (repo.updateCategory(selected.getId(), newName)) {
            showAlert("Category updated!");
            loadCategories("");
        } else {
            showAlert("Error updating category.");
        }
    }

    // =====================================================
    // DELETE CATEGORY
    // =====================================================
    private void deleteCategory() {
        Category selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a category to delete.");
            return;
        }

        if (repo.deleteCategory(selected.getId())) {
            showAlert("Category deleted!");
            loadCategories("");
            nameField.clear();
        } else {
            showAlert("Error deleting category.");
        }
    }

    // =====================================================
    // ALERT HELPER
    // =====================================================
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
