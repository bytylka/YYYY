package com.store.app.models;

public class Review {
    private int id;
    private int userId;
    private int productId;
    private int rating;
    private String comment;
    private String username;
    public Review(int id, int userId, int productId, int rating, String comment, String username) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.username = username;
    }
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getUsername() { return username != null ? username : "Аноним"; }
}