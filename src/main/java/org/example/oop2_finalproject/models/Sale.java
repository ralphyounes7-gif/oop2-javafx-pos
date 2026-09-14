package org.example.oop2_finalproject.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Sale {

    private final IntegerProperty id;
    private final IntegerProperty cashierId;
    private final ObjectProperty<Integer> customerId;
    private final ObjectProperty<LocalDateTime> date;
    private final DoubleProperty subtotal;
    private final DoubleProperty tax;
    private final DoubleProperty discount;
    private final DoubleProperty total;

    public Sale(int id, int cashierId, Integer customerId,
                LocalDateTime date, double subtotal, double tax,
                double discount, double total) {

        this.id = new SimpleIntegerProperty(id);
        this.cashierId = new SimpleIntegerProperty(cashierId);
        this.customerId = new SimpleObjectProperty<>(customerId);
        this.date = new SimpleObjectProperty<>(date);
        this.subtotal = new SimpleDoubleProperty(subtotal);
        this.tax = new SimpleDoubleProperty(tax);
        this.discount = new SimpleDoubleProperty(discount);
        this.total = new SimpleDoubleProperty(total);
    }

    // GETTERS
    public int getId() { return id.get(); }
    public int getCashierId() { return cashierId.get(); }
    public Integer getCustomerId() { return customerId.get(); }
    public LocalDateTime getDate() { return date.get(); }
    public double getSubtotal() { return subtotal.get(); }
    public double getTax() { return tax.get(); }
    public double getDiscount() { return discount.get(); }
    public double getTotal() { return total.get(); }

    // PROPERTY GETTERS (for TableView)
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty cashierIdProperty() { return cashierId; }
    public ObjectProperty<Integer> customerIdProperty() { return customerId; }
    public ObjectProperty<LocalDateTime> dateProperty() { return date; }
    public DoubleProperty subtotalProperty() { return subtotal; }
    public DoubleProperty taxProperty() { return tax; }
    public DoubleProperty discountProperty() { return discount; }
    public DoubleProperty totalProperty() { return total; }
}
