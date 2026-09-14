package org.example.oop2_finalproject.models;

import javafx.beans.property.*;

public class Category {

    private final IntegerProperty id;
    private final StringProperty name;

    public Category(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    // Property getters for TableView
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return getName();
    }

}