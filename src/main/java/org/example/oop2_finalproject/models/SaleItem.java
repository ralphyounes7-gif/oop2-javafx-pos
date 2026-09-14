package org.example.oop2_finalproject.models;

import javafx.beans.property.*;

public class SaleItem {

    private final IntegerProperty id;
    private final IntegerProperty saleId;
    private final IntegerProperty productId;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final DoubleProperty total;

    public SaleItem(int id, int saleId, int productId, int quantity, double price, double total) {
        this.id = new SimpleIntegerProperty(id);
        this.saleId = new SimpleIntegerProperty(saleId);
        this.productId = new SimpleIntegerProperty(productId);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.total = new SimpleDoubleProperty(total);
    }

    // GETTERS
    public int getId() { return id.get(); }
    public int getSaleId() { return saleId.get(); }
    public int getProductId() { return productId.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getPrice() { return price.get(); }
    public double getTotal() { return total.get(); }

    // PROPERTY GETTERS
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty saleIdProperty() { return saleId; }
    public IntegerProperty productIdProperty() { return productId; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty priceProperty() { return price; }
    public DoubleProperty totalProperty() { return total; }
}
