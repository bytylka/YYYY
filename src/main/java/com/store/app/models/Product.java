package com.store.app.models;
public class Product {
    private int id;
    private String title;
    private String description;
    private double price;
    private String type;
    private double averageRating;
    public Product(int id, String title, String description, double price, String type) {
        this(id, title, description, price, type, 0.0);
    }
    public Product(int id, String title, String description, double price, String type, double averageRating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = type;
        this.averageRating = averageRating;
    }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getType() { return type; }
    public double getAverageRating() { return averageRating; }

    public String getRatingStars() {
        return "★".repeat((int) Math.round(averageRating)) +
                String.format(" %.1f", averageRating);
    }
}