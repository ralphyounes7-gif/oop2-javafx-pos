package org.example.oop2_finalproject.views.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import org.example.oop2_finalproject.Database_Service.*;
import org.example.oop2_finalproject.models.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class SalesReportView {

    private BorderPane root;

    private TableView<Sale> salesTable;
    private TableView<SaleItem> itemsTable;

    private SalesRepository salesRepo = new SalesRepository();
    private SaleItemsRepository itemsRepo = new SaleItemsRepository();
    private CustomersRepository customersRepo = new CustomersRepository();
    private CashiersRepository cashiersRepo = new CashiersRepository();
    private ProductsRepository productsRepo = new ProductsRepository();

    private ObservableList<Sale> salesList = FXCollections.observableArrayList();
    private ObservableList<SaleItem> itemsList = FXCollections.observableArrayList();

    private HashMap<Integer, String> cashierNames = new HashMap<>();
    private HashMap<Integer, String> customerNames = new HashMap<>();
    private HashMap<Integer, String> productNames = new HashMap<>();

    public BorderPane getRoot() {
        return root;
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public SalesReportView() {
        root = new BorderPane();
        root.setPadding(new Insets(15));

        loadNames();
        buildTopFilters();
        buildTables();

        loadAllSales();
    }

    // =====================================================
    // BUILD TOP FILTER BAR
    // =====================================================
    private void buildTopFilters() {

        Text title = new Text("Sales Report");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by Sale ID...");

        Button searchBtn = new Button("Search");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Filter by Date");

        Button resetBtn = new Button("Reset");

        HBox bar = new HBox(10, title, searchField, searchBtn, datePicker, resetBtn);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(10));

        searchBtn.setOnAction(e -> {
            try {
                int saleId = Integer.parseInt(searchField.getText().trim());
                Sale s = salesRepo.getSaleById(saleId);

                salesList.clear();
                if (s != null) salesList.add(s);

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Sale ID");
                alert.show();
            }
        });

        datePicker.setOnAction(e -> {
            if (datePicker.getValue() != null) {
                List<Sale> list = salesRepo.getSalesByDate(datePicker.getValue().toString());
                salesList.setAll(list);
            }
        });

        resetBtn.setOnAction(e -> loadAllSales());

        root.setTop(bar);
    }

    // =====================================================
    // BUILD TABLES
    // =====================================================
    private void buildTables() {
        salesTable = new TableView<>();
        itemsTable = new TableView<>();

        buildSalesTableColumns();
        buildItemsTableColumns();

        salesTable.setItems(salesList);
        itemsTable.setItems(itemsList);

        salesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                List<SaleItem> list = itemsRepo.getSaleItems(selected.getId());
                itemsList.setAll(list);
            }
        });

        VBox right = new VBox(10, new Text("Sale Items"), itemsTable);
        right.setPadding(new Insets(10));
        right.setPrefWidth(400);

        root.setCenter(salesTable);
        root.setRight(right);
    }

    // =====================================================
    // SALES TABLE COLUMNS
    // =====================================================
    private void buildSalesTableColumns() {

        TableColumn<Sale, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()));

        TableColumn<Sale, String> cashierCol = new TableColumn<>("Cashier");
        cashierCol.setCellValueFactory(c -> {
            int cid = c.getValue().getCashierId();
            return new javafx.beans.property.SimpleStringProperty(cashierNames.getOrDefault(cid, "Unknown"));
        });

        TableColumn<Sale, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(c -> {
            Integer cid = c.getValue().getCustomerId();
            String name = (cid == null) ? "Guest" : customerNames.getOrDefault(cid, "Unknown");
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        TableColumn<Sale, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getDate().toString()
                )
        );

        TableColumn<Sale, Number> totalCol = new TableColumn<>("Total ($)");
        totalCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotal())
        );

        salesTable.getColumns().addAll(idCol, cashierCol, customerCol, dateCol, totalCol);
        salesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // =====================================================
    // SALE ITEMS TABLE COLUMNS
    // =====================================================
    private void buildItemsTableColumns() {

        TableColumn<SaleItem, Number> pidCol = new TableColumn<>("Product");
        pidCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getProductId()));

        pidCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("");
                else setText(productNames.getOrDefault(item.intValue(), "Unknown Product"));
            }
        });

        TableColumn<SaleItem, Number> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantity()));

        TableColumn<SaleItem, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrice()));

        TableColumn<SaleItem, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotal()));

        itemsTable.getColumns().addAll(pidCol, qtyCol, priceCol, totalCol);
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // =====================================================
    // LOAD NAMES FOR DISPLAY
    // =====================================================
    private void loadNames() {
        cashiersRepo.getAllCashiers().forEach(c ->
                cashierNames.put(c.getId(), c.getUsername())
        );

        customersRepo.getAllCustomers().forEach(c ->
                customerNames.put(c.getId(), c.getName())
        );

        productsRepo.getAllProducts().forEach(p ->
                productNames.put(p.getId(), p.getName())
        );
    }

    // =====================================================
    // LOAD SALES
    // =====================================================
    private void loadAllSales() {
        salesList.setAll(salesRepo.getAllSales());
        itemsList.clear();
    }
}
