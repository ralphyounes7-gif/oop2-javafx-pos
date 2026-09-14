package org.example.oop2_finalproject.views.cashier;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.oop2_finalproject.models.Product;
import org.example.oop2_finalproject.utils.CartData;

public class CartView extends VBox {

    private TableView<CartData.CartItem> table;
    private Label subtotalLabel;
    private Label totalLabel;
    private Label discountLabel;

    public CartView() {
        setSpacing(15);
        setPadding(new Insets(20));

        Text title = new Text("Cart");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        table = new TableView<>();
        buildTable();

        subtotalLabel = new Label();
        totalLabel = new Label();
        discountLabel = new Label("Discount: $0.00");

        updateSummary();

        Button checkoutBtn = new Button("Proceed to Checkout");

        getChildren().addAll(
                title,
                table,
                subtotalLabel,
                discountLabel,
                totalLabel,
                checkoutBtn
        );
    }

    // ============================================================
    // BUILD CART TABLE
    // ============================================================
    private void buildTable() {

        TableColumn<CartData.CartItem, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().product.getId())
        );

        TableColumn<CartData.CartItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().product.getName())
        );

        TableColumn<CartData.CartItem, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleDoubleProperty(c.getValue().product.getPrice())
        );

        // ==========================
        // QUANTITY COLUMN (Editable)
        // ==========================
        TableColumn<CartData.CartItem, Number> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellFactory(col -> new TableCell<>() {

            private final TextField qtyField = new TextField();

            {
                qtyField.setPrefWidth(50);

                qtyField.setOnAction(e -> updateQty());
                qtyField.focusedProperty().addListener((obs, old, newVal) -> {
                    if (!newVal) updateQty();
                });
            }

            private void updateQty() {
                try {
                    int newQty = Integer.parseInt(qtyField.getText());
                    if (newQty <= 0) return;

                    CartData.CartItem item = getTableView().getItems().get(getIndex());
                    item.quantity = newQty;

                    table.refresh();
                    updateSummary();

                } catch (Exception ignored) {}
            }

            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() >= table.getItems().size()) {
                    setGraphic(null);
                    return;
                }

                qtyField.setText(String.valueOf(table.getItems().get(getIndex()).quantity));
                setGraphic(qtyField);
            }
        });

        // ==========================
        // TOTAL
        // ==========================
        TableColumn<CartData.CartItem, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleDoubleProperty(
                        c.getValue().product.getPrice() * c.getValue().quantity
                )
        );

        // ==========================
        // REMOVE BUTTON
        // ==========================
        TableColumn<CartData.CartItem, Void> removeCol = new TableColumn<>("Remove");
        removeCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("X");

            {
                btn.setOnAction(e -> {
                    CartData.CartItem item = getTableView().getItems().get(getIndex());
                    CartData.getCart().remove(item);

                    loadItems();
                    updateSummary();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(idCol, nameCol, priceCol, qtyCol, totalCol, removeCol);

        loadItems();
    }

    // ============================================================
    // LOAD ITEMS INTO TABLE
    // ============================================================
    private void loadItems() {
        table.setItems(CartData.getCart());
    }

    // ============================================================
    // UPDATE SUMMARY LABELS
    // ============================================================
    private void updateSummary() {

        double subtotal = 0;

        for (CartData.CartItem item : CartData.getCart()) {
            subtotal += item.product.getPrice() * item.quantity;
        }

        double discount = 0;
        double total = subtotal - discount;

        subtotalLabel.setText(String.format("Subtotal: $%.2f", subtotal));
        discountLabel.setText(String.format("Discount: $%.2f", discount));
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
}
