package org.example.oop2_finalproject.views.cashier;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.oop2_finalproject.Database_Service.CategoriesRepository;
import org.example.oop2_finalproject.Database_Service.ProductsRepository;
import org.example.oop2_finalproject.models.Category;
import org.example.oop2_finalproject.models.Product;
import org.example.oop2_finalproject.utils.CartData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSearchView extends VBox {

    private final ProductsRepository productsRepo = new ProductsRepository();
    private final CategoriesRepository categoriesRepo = new CategoriesRepository();

    private TableView<Product> table;
    private TextField searchField;
    private ComboBox<Category> categoryFilter;

    private List<Category> cachedCategories = new ArrayList<>();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

    // Pagination
    private Pagination pagination;
    private final int ROWS_PER_PAGE = 10;

    public ProductSearchView() {
        setPadding(new Insets(20));
        setSpacing(15);

        buildUI();          // 1 — Build UI first
        loadCategories();   // 2 — Load categories
        loadProducts();     // 3 — Load products
        applyFilters();     // 4 — Show table
    }

    // ================================
    // BUILD UI
    // ================================
    private void buildUI() {
        Text title = new Text("Product Search");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Search area
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search by name or barcode...");
        searchField.setPrefWidth(300);

        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> applyFilters());

        categoryFilter = new ComboBox<>();
        categoryFilter.setPrefWidth(200);
        categoryFilter.setOnAction(e -> applyFilters());

        searchBox.getChildren().addAll(
                new Text("Search:"), searchField,
                new Text("Category:"), categoryFilter,
                searchBtn
        );

        // Table
        table = new TableView<>();
        buildTableColumns();

        pagination = new Pagination();
        pagination.setPageFactory(this::createPage);

        getChildren().addAll(title, searchBox, table, pagination);
    }

    // ================================
    // LOAD DATA
    // ================================
    private void loadCategories() {
        cachedCategories = categoriesRepo.getAllCategories();

        categoryFilter.getItems().clear();
        categoryFilter.getItems().add(new Category(0, "All Categories"));
        categoryFilter.getItems().addAll(cachedCategories);
        categoryFilter.getSelectionModel().selectFirst();
    }

    private void loadProducts() {
        allProducts.setAll(productsRepo.getAllProducts());
    }

    // ================================
    // FILTER LOGIC
    // ================================
    private void applyFilters() {
        String keyword = searchField.getText().trim().toLowerCase();
        Category selected = categoryFilter.getSelectionModel().getSelectedItem();

        filteredProducts.setAll(allProducts.stream()
                .filter(p ->
                        (keyword.isEmpty() ||
                                p.getName().toLowerCase().contains(keyword) ||
                                p.getBarcode().toLowerCase().contains(keyword))
                                &&
                                (selected == null || selected.getId() == 0 || p.getCategoryId() == selected.getId())
                )
                .collect(Collectors.toList()));

        updatePagination();
    }

    private void updatePagination() {
        int pageCount = (int) Math.ceil((double) filteredProducts.size() / ROWS_PER_PAGE);
        if (pageCount == 0) pageCount = 1;
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    // ================================
    // PAGINATION PAGE CREATOR
    // ================================
    private VBox createPage(int pageIndex) {
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, filteredProducts.size());
        table.setItems(FXCollections.observableArrayList(filteredProducts.subList(start, end)));

        return new VBox(table);
    }

    // ================================
    // TABLE COLUMNS
    // ================================
    private void buildTableColumns() {

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, Number> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(50);
        idCol.setMaxWidth(70);
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(c -> c.getValue().idProperty());

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setPrefWidth(180);
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setPrefWidth(150);
        categoryCol.setCellValueFactory(c -> {
            int cid = c.getValue().getCategoryId();
            String name = cachedCategories.stream()
                    .filter(cat -> cat.getId() == cid)
                    .map(Category::getName)
                    .findFirst()
                    .orElse("Unknown");
            return new SimpleStringProperty(name);
        });

        TableColumn<Product, Number> priceCol = new TableColumn<>("Price");
        priceCol.setPrefWidth(80);
        priceCol.setCellValueFactory(c -> c.getValue().priceProperty());

        TableColumn<Product, Number> stockCol = new TableColumn<>("Stock");
        stockCol.setPrefWidth(80);
        stockCol.setCellValueFactory(c -> c.getValue().stockProperty());

        TableColumn<Product, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setPrefWidth(120);
        barcodeCol.setCellValueFactory(c -> c.getValue().barcodeProperty());

        TableColumn<Product, Void> addCol = new TableColumn<>("Add");
        addCol.setPrefWidth(70);
        addCol.setMaxWidth(70);
        addCol.setMinWidth(70);

        addCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Add");

            {
                btn.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    showQuantityDialog(p);
                });
                btn.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(btn);
            }
        });

        table.getColumns().addAll(idCol, nameCol, categoryCol, priceCol, stockCol, barcodeCol, addCol);
    }


    // ================================
    // ADD TO CART
    // ================================
    private void showQuantityDialog(Product p) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add to Cart");
        dialog.setHeaderText("Add product: " + p.getName());
        dialog.setContentText("Enter quantity:");

        dialog.showAndWait().ifPresent(qtyStr -> {
            try {
                int qty = Integer.parseInt(qtyStr);
                if (qty <= 0 || qty > p.getStock()) {
                    showAlert("Invalid quantity!");
                    return;
                }

                CartData.addItem(p, qty);

                showAlert("Added " + qty + " x " + p.getName());

            } catch (Exception ex) {
                showAlert("Invalid number!");
            }
        });
    }


    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.show();
    }
}
