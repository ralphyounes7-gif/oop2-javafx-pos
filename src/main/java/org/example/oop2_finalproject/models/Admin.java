package org.example.oop2_finalproject.models;

import javafx.beans.property.*;

public class Admin {

    private final IntegerProperty id;
    private final StringProperty username;
    private final StringProperty password;

    public Admin(int id, String username, String password) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    // GETTERS
    public int getId() { return id.get(); }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }

    // PROPERTY GETTERS (for TableView)
    public IntegerProperty idProperty() { return id; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }

    // SETTERS
    public void setUsername(String username) { this.username.set(username); }
    public void setPassword(String password) { this.password.set(password); }

    @Override
    public String toString() {
        return getUsername();
    }
}
