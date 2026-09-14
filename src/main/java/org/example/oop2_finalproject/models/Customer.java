package org.example.oop2_finalproject.models;

import javafx.beans.property.*;

public class Customer {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty phone;
    private final IntegerProperty points;

    public Customer(int id, String name, String phone, int points) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.points = new SimpleIntegerProperty(points);
    }

    // ==========================
    // GETTERS
    // ==========================
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getPhone() { return phone.get(); }
    public int getPoints() { return points.get(); }

    // ==========================
    // PROPERTY GETTERS
    // ==========================
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty phoneProperty() { return phone; }
    public IntegerProperty pointsProperty() { return points; }

    // ==========================
    // SETTERS
    // ==========================
    public void setName(String name) { this.name.set(name); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setPoints(int points) { this.points.set(points); }

    @Override
    public String toString() {
        return getName();
    }
}
