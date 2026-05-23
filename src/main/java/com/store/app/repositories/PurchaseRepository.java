package com.store.app.repositories;

import com.store.app.database.DatabaseHandler;
import com.store.app.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class PurchaseRepository {
    public void addPurchase(int userId, int productId) {
        String sql = "INSERT INTO purchases (user_id, product_id) VALUES (?, ?) " +
                "ON CONFLICT (user_id, product_id) DO NOTHING";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isPurchased(int userId, int productId) {
        String sql = "SELECT 1 FROM purchases WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Product> getPurchasedProducts(int userId) {
        List<Product> products = new ArrayList<>();
        String sql = """
        SELECT p.* FROM products p
        JOIN purchases pur ON p.id = pur.product_id
        WHERE pur.user_id = ?
        ORDER BY pur.purchased_at DESC
        """;
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("product_type")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}