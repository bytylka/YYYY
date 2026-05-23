package com.store.app.repositories;

import com.store.app.database.DatabaseHandler;
import com.store.app.models.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {
    public void addReview(int userId, int productId, int rating, String comment) {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, comment) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (user_id, product_id) DO UPDATE SET " +
                "rating = EXCLUDED.rating, comment = EXCLUDED.comment";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, rating);
            pstmt.setString(4, comment);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Review> getReviewsByProduct(int productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.username FROM reviews r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(new Review(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getString("username")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}