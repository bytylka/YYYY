package com.store.app.repositories;

import com.store.app.database.DatabaseHandler;
import com.store.app.models.User;
import com.store.app.utils.SecurityUtils;
import java.sql.*;

public class UserRepository {
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        String hashedPassword = SecurityUtils.hashSHA256(password);
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("avatar_url"),
                            rs.getString("about_me"),
                            rs.getString("theme_preference")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String username, String password) {
        String hashedPassword = SecurityUtils.hashSHA256(password);

        try (Connection conn = DatabaseHandler.getConnection();
             CallableStatement cs = conn.prepareCall("CALL register_user(?, ?, ?)")) {

            cs.setString(1, username);
            cs.setString(2, hashedPassword);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Ошибка регистрации: " + e.getMessage());
            return false;
        }
    }

    public void saveProfile(User sessionUser) {
        if (sessionUser == null) return;

        try (Connection conn = DatabaseHandler.getConnection();
             CallableStatement cs = conn.prepareCall("CALL update_user_profile(?, ?, ?, ?, ?)")) {

            cs.setInt(1, sessionUser.getId());
            cs.setString(2, sessionUser.getUsername());
            cs.setString(3, sessionUser.getAbout() != null ? sessionUser.getAbout() : "");
            cs.setString(4, sessionUser.getAvatar() != null ? sessionUser.getAvatar() : "default_avatar.png");
            cs.setString(5, sessionUser.getTheme() != null ? sessionUser.getTheme() : "DARK");

            cs.execute();
            System.out.println("Профиль сохранён: " + sessionUser.getUsername());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}