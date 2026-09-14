package org.example.oop2_finalproject.models;

import javafx.beans.property.*;

public class Product {

    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty categoryId;
    private final StringProperty categoryName;   // optional → used for displaying category name in table
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final StringProperty barcode;

    // Constructor with category name
    public Product(int id, String name, int categoryId, String categoryName,
                   double price, int stock, String barcode) {

        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.categoryId = new SimpleIntegerProperty(categoryId);
        this.categoryName = new SimpleStringProperty(categoryName);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.barcode = new SimpleStringProperty(barcode);
    }

    // Constructor WITHOUT category name (fallback)
    public Product(int id, String name, int categoryId,
                   double price, int stock, String barcode) {

        this(id, name, categoryId, "", price, stock, barcode);
    }

    // =============================
    // GETTERS
    // =============================

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public int getCategoryId() { return categoryId.get(); }
    public String getCategoryName() { return categoryName.get(); }
    public double getPrice() { return price.get(); }
    public int getStock() { return stock.get(); }
    public String getBarcode() { return barcode.get(); }

    // =============================
    // PROPERTY GETTERS (for TableView)
    // =============================

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty categoryIdProperty() { return categoryId; }
    public StringProperty categoryNameProperty() { return categoryName; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty stockProperty() { return stock; }
    public StringProperty barcodeProperty() { return barcode; }

    // =============================
    // SETTERS
    // =============================

    public void setName(String name) { this.name.set(name); }
    public void setCategoryId(int categoryId) { this.categoryId.set(categoryId); }
    public void setCategoryName(String name) { this.categoryName.set(name); }
    public void setPrice(double price) { this.price.set(price); }
    public void setStock(int stock) { this.stock.set(stock); }
    public void setBarcode(String barcode) { this.barcode.set(barcode); }


}
