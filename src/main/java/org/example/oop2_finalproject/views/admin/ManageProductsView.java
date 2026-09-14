package org.example.oop2_finalproject.views.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.oop2_finalproject.Database_Service.CategoriesRepository;
import org.example.oop2_finalproject.Database_Service.ProductsRepository;
import org.example.oop2_finalproject.models.Category;
import org.example.oop2_finalproject.models.Product;

import java.util.List;

public class ManageProductsView extends BorderPane {

    private final ProductsRepository productRepo = new ProductsRepository();
    private final CategoriesRepository categoryRepo = new CategoriesRepository();

    // FORM INPUTS
    private TextField nameField, priceField, stockField, barcodeField, searchField;
    private ComboBox<Category> categoryCombo;

    // TABLE
    private TableView<Product> table;
    private ObservableList<Product> displayedProducts;

    // PAGINATION
    private final int ITEMS_PER_PAGE = 10;
    private int currentPage = 1;
    private int totalPages = 1;

    // FILTER CATEGORY
    private ComboBox<Category> filterCategoryCombo;

    public ManageProductsView() {
        buildUI();
        loadAllProducts();  // initial load
    }

    // ===========================================================
    // UI BUILDING
    // ===========================================================
    private void buildUI() {

        // ==========================
        // TOP SECTION: Search + Filter
        // ==========================
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search product...");
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> searchProducts());

        // Filter by category
        filterCategoryCombo = new ComboBox<>();
        filterCategoryCombo.setPromptText("Filter by Category");
        loadCategoryFilter();
        filterCategoryCombo.setOnAction(e -> filterByCategory());

        searchBox.getChildren().addAll(searchField, searchBtn, filterCategoryCombo);

        topSection.getChildren().add(searchBox);

        // ==========================
        // CENTER TABLE
        // ==========================
        table = new TableView<>();

        TableColumn<Product, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(p -> p.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Product, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(p -> p.getValue().nameProperty());
        colName.setPrefWidth(150);

        TableColumn<Product, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(p -> p.getValue().categoryNameProperty());
        colCategory.setPrefWidth(120);

        TableColumn<Product, Number> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(p -> p.getValue().priceProperty());
        colPrice.setPrefWidth(90);

        TableColumn<Product, Number> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(p -> p.getValue().stockProperty());
        colStock.setPrefWidth(80);

        TableColumn<Product, String> colBarcode = new TableColumn<>("Barcode");
        colBarcode.setCellValueFactory(p -> p.getValue().barcodeProperty());
        colBarcode.setPrefWidth(120);

        table.getColumns().addAll(colId, colName, colCategory, colPrice, colStock, colBarcode);

        // When selecting product → fill the form
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null)
                fillForm(selected);
        });

        // ==========================
        // BOTTOM FORM SECTION
        // ==========================
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        nameField = new TextField();
        priceField = new TextField();
        stockField = new TextField();
        barcodeField = new TextField();
        categoryCombo = new ComboBox<>();
        loadCategoryCombo();

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);

        form.add(new Label("Category:"), 0, 1);
        form.add(categoryCombo, 1, 1);

        form.add(new Label("Price:"), 0, 2);
        form.add(priceField, 1, 2);

        form.add(new Label("Stock:"), 0, 3);
        form.add(stockField, 1, 3);

        form.add(new Label("Barcode:"), 0, 4);
        form.add(barcodeField, 1, 4);

        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        addBtn.setOnAction(e -> addProduct());
        updateBtn.setOnAction(e -> updateProduct());
        deleteBtn.setOnAction(e -> deleteProduct());
        refreshBtn.setOnAction(e -> {
            searchField.clear();
            filterCategoryCombo.getSelectionModel().clearSelection();
            loadAllProducts();
        });

        btnBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, refreshBtn);

        // ==========================
        // PAGINATION SECTION
        // ==========================
        HBox paginationBox = new HBox(10);
        paginationBox.setAlignment(Pos.CENTER);

        Button prevPage = new Button("<< Previous");
        Button nextPage = new Button("Next >>");

        Label pageLabel = new Label();

        prevPage.setOnAction(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateTablePage();
            }
        });

        nextPage.setOnAction(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateTablePage();
            }
        });

        paginationBox.getChildren().addAll(prevPage, pageLabel, nextPage);

        // Add everything
        formSection.getChildren().addAll(form, btnBox, paginationBox);

        // ADD ALL TO LAYOUT
        this.setTop(topSection);
        this.setCenter(table);
        this.setBottom(formSection);
    }

    // ===========================================================
    // LOAD CATEGORIES
    // ===========================================================
    private void loadCategoryCombo() {
        List<Category> list = categoryRepo.getAllCategories();
        categoryCombo.setItems(FXCollections.observableArrayList(list));
    }

    private void loadCategoryFilter() {
        List<Category> list = categoryRepo.getAllCategories();
        Category all = new Category(0, "All Categories");
        list.add(0, all);
        filterCategoryCombo.setItems(FXCollections.observableArrayList(list));
    }

    // ===========================================================
    // TABLE LOADING + PAGINATION
    // ===========================================================
    private void loadAllProducts() {
        List<Product> list = productRepo.getAllProducts();
        displayedProducts = FXCollections.observableArrayList(withCategoryNames(list));

        setupPagination();
    }

    private void searchProducts() {
        String keyword = searchField.getText().trim();
        List<Product> list = productRepo.searchProducts(keyword);
        displayedProducts = FXCollections.observableArrayList(withCategoryNames(list));

        setupPagination();
    }

    private void filterByCategory() {
        Category selected = filterCategoryCombo.getSelectionModel().getSelectedItem();
        if (selected == null || selected.getId() == 0) {
            loadAllProducts();
            return;
        }

        List<Product> list = productRepo.getProductsByCategory(selected.getId());
        displayedProducts = FXCollections.observableArrayList(withCategoryNames(list));

        setupPagination();
    }

    // Converts products to include category name
    private List<Product> withCategoryNames(List<Product> list) {
        List<Category> categories = categoryRepo.getAllCategories();

        for (Product p : list) {
            for (Category c : categories) {
                if (p.getCategoryId() == c.getId()) {
                    p.setCategoryName(c.getName());
                }
            }
        }
        return list;
    }

    private void setupPagination() {
        int totalItems = displayedProducts.size();
        totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        currentPage = 1;
        updateTablePage();
    }

    private void updateTablePage() {
        int fromIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, displayedProducts.size());

        table.setItems(FXCollections.observableArrayList(
                displayedProducts.subList(fromIndex, toIndex)
        ));
    }

    // ===========================================================
    // CRUD METHODS
    // ===========================================================
    private void addProduct() {
        String name = nameField.getText().trim();
        Category category = categoryCombo.getValue();
        String priceStr = priceField.getText().trim();
        String stockStr = stockField.getText().trim();
        String barcode = barcodeField.getText().trim();

        if (name.isEmpty() || category == null || priceStr.isEmpty() || stockStr.isEmpty()) {
            showAlert("Fill all required fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            if (productRepo.addProduct(name, category.getId(), price, stock, barcode)) {
                showAlert("Product added!");
                loadAllProducts();
                clearForm();
            } else {
                showAlert("Error adding product.");
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid number format.");
        }
    }

    private void updateProduct() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a product to update.");
            return;
        }

        String name = nameField.getText().trim();
        Category category = categoryCombo.getValue();
        String priceStr = priceField.getText().trim();
        String stockStr = stockField.getText().trim();
        String barcode = barcodeField.getText().trim();

        if (name.isEmpty() || category == null || priceStr.isEmpty() || stockStr.isEmpty()) {
            showAlert("Fill all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            if (productRepo.updateProduct(
                    selected.getId(),
                    name, category.getId(), price, stock, barcode)) {

                showAlert("Product updated!");
                loadAllProducts();
                clearForm();

            } else {
                showAlert("Error updating product.");
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid number format.");
        }
    }

    private void deleteProduct() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a product to delete.");
            return;
        }

        if (productRepo.deleteProduct(selected.getId())) {
            showAlert("Product deleted!");
            loadAllProducts();
            clearForm();
        } else {
            showAlert("Error deleting product.");
        }
    }

    // ===========================================================
    // HELPERS
    // ===========================================================
    private void fillForm(Product p) {
        nameField.setText(p.getName());
        priceField.setText(String.valueOf(p.getPrice()));
        stockField.setText(String.valueOf(p.getStock()));
        barcodeField.setText(p.getBarcode());

        // select category in ComboBox
        for (Category c : categoryCombo.getItems()) {
            if (c.getId() == p.getCategoryId()) {
                categoryCombo.getSelectionModel().select(c);
            }
        }
    }

    private void clearForm() {
        nameField.clear();
        priceField.clear();
        stockField.clear();
        barcodeField.clear();
        categoryCombo.getSelectionModel().clearSelection();
        table.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
