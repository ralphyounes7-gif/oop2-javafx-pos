package org.example.oop2_finalproject.views.cashier;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.oop2_finalproject.Database_Service.*;
import org.example.oop2_finalproject.models.*;
import org.example.oop2_finalproject.utils.CartData;

import java.util.List;

public class CheckoutView extends VBox {

    private final CustomersRepository customersRepo = new CustomersRepository();
    private final SalesRepository salesRepo = new SalesRepository();
    private final SaleItemsRepository saleItemsRepo = new SaleItemsRepository();

    private ComboBox<Customer> customerCombo;
    private Label subtotalLabel, taxLabel, discountLabel, totalLabel;
    private TextField discountField;
    private Button confirmBtn;

    private double subtotal = 0;
    private double tax = 0;
    private double discount = 0;
    private double total = 0;

    public CheckoutView() {
        setPadding(new Insets(20));
        setSpacing(20);

        buildUI();
        loadCustomers();
        calculateTotals();
    }

    private void buildUI() {

        Text title = new Text("Checkout");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Customer selection
        customerCombo = new ComboBox<>();
        customerCombo.setPrefWidth(250);
        customerCombo.setPromptText("Select Customer (optional)");

        // Summary labels
        subtotalLabel = new Label();
        taxLabel = new Label();
        discountLabel = new Label();
        totalLabel = new Label();

        // Discount input
        HBox discountBox = new HBox(10);
        discountBox.setAlignment(Pos.CENTER_LEFT);
        discountField = new TextField();
        discountField.setPrefWidth(80);
        discountField.setPromptText("%");

        Button applyDiscountBtn = new Button("Apply");
        applyDiscountBtn.setOnAction(e -> applyDiscount());

        discountBox.getChildren().addAll(new Text("Discount (%): "), discountField, applyDiscountBtn);

        // Confirm
        confirmBtn = new Button("Confirm Sale");
        confirmBtn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        confirmBtn.setOnAction(e -> confirmSale());

        getChildren().addAll(
                title,
                new Text("Customer:"), customerCombo,
                new Separator(),
                subtotalLabel,
                taxLabel,
                discountLabel,
                totalLabel,
                discountBox,
                new Separator(),
                confirmBtn
        );
    }

    // ====================================================
    // LOAD CUSTOMERS INTO THE DROPDOWN
    // ====================================================
    private void loadCustomers() {
        List<Customer> list = customersRepo.getAllCustomers();
        customerCombo.getItems().clear();
        customerCombo.getItems().add(null); // "No customer"
        customerCombo.getItems().addAll(list);

        customerCombo.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "None" : item.getName() + " (" + item.getPhone() + ")");
            }
        });

        customerCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "No Customer" : item.getName());
            }
        });

        customerCombo.getSelectionModel().selectFirst();
    }

    // ====================================================
    // CALCULATE TOTALS
    // ====================================================
    private void calculateTotals() {

        subtotal = 0;
        for (CartData.CartItem item : CartData.getCart()) {
            subtotal += item.getTotal();
        }

        tax = subtotal * 0.11; // 11% VAT or modify if needed
        total = subtotal + tax - discount;

        updateLabels();
    }

    private void updateLabels() {
        subtotalLabel.setText(String.format("Subtotal: $%.2f", subtotal));
        taxLabel.setText(String.format("Tax (11%%): $%.2f", tax));
        discountLabel.setText(String.format("Discount: -$%.2f", discount));
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void applyDiscount() {
        try {
            double percent = Double.parseDouble(discountField.getText());
            if (percent < 0 || percent > 100) {
                alert("Invalid discount: 0–100% allowed");
                return;
            }

            discount = subtotal * (percent / 100);
            calculateTotals();

        } catch (Exception e) {
            alert("Invalid discount number");
        }
    }

    // ====================================================
    // CONFIRM SALE
    // ====================================================
    private void confirmSale() {

        if (CartData.getCart().isEmpty()) {
            alert("Cart is empty!");
            return;
        }

        Customer selected = customerCombo.getSelectionModel().getSelectedItem();
        Integer customerId = (selected == null ? null : selected.getId());

        // Create sale first → get sale ID
        int saleId = salesRepo.createSale(
                1,                    // cashier ID (replace with logged-in cashier later)
                customerId,
                subtotal,
                tax,
                discount,
                total
        );

        if (saleId == -1) {
            alert("Error creating sale!");
            return;
        }

        // Save sale items
        for (CartData.CartItem item : CartData.getCart()) {
            saleItemsRepo.addSaleItem(
                    saleId,
                    item.product.getId(),
                    item.quantity,
                    item.product.getPrice(),
                    item.getTotal()
            );
        }

        // Update customer points (optional)
        if (selected != null) {
            int newPoints = selected.getPoints() + (int) total;
            customersRepo.updateCustomerPoints(selected.getId(), newPoints);
        }

        // Clear cart
        CartData.clear();
        alert("Sale completed!");

        calculateTotals(); // refresh labels
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.show();
    }
}
