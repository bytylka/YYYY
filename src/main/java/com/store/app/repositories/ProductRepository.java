package com.store.app.repositories;

import com.store.app.database.DatabaseHandler;
import com.store.app.models.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ProductRepository {
    public List<Product> getAllProducts(String sortBy, String category) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT p.*, COALESCE(AVG(r.rating), 0) as avg_rating 
            FROM products p
            LEFT JOIN reviews r ON p.id = r.product_id
            """);

        if (category != null && !category.equals("ALL")) {
            sql.append(" WHERE p.product_type = ?");
        }
        sql.append(" GROUP BY p.id");
        switch (sortBy) {
            case "price_asc" -> sql.append(" ORDER BY p.price ASC");
            case "price_desc" -> sql.append(" ORDER BY p.price DESC");
            case "name" -> sql.append(" ORDER BY p.title ASC");
            case "rating" -> sql.append(" ORDER BY avg_rating DESC");
            default -> sql.append(" ORDER BY p.id DESC");
        }
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            if (category != null && !category.equals("ALL")) {
                pstmt.setString(1, category);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("product_type"),
                            rs.getDouble("avg_rating")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public List<Product> getAllProducts() {
        return getAllProducts("default", "ALL");
    }
}