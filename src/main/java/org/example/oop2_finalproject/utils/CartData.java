package org.example.oop2_finalproject.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.oop2_finalproject.models.Product;

public class CartData {

    public static class CartItem {
        public Product product;
        public int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public double getTotal() {
            return product.getPrice() * quantity;
        }
    }

    private static final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    public static ObservableList<CartItem> getCart() {
        return cartItems;
    }

    public static void addItem(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.product.getId() == product.getId()) {
                item.quantity += quantity;
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity));
    }

    public static void clear() {
        cartItems.clear();
    }
}
