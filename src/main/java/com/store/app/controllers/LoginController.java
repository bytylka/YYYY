package com.store.app.controllers;

import com.store.app.models.User;
import com.store.app.repositories.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Заполните все поля!");
            return;
        }

        User authenticatedUser = userRepository.login(user, pass);

        if (authenticatedUser != null) {
            statusLabel.setStyle("-fx-text-fill: #a6e3a1;");
            statusLabel.setText("Успех! Входим...");
            openProfile(authenticatedUser);
        } else {
            statusLabel.setStyle("-fx-text-fill: #f38ba8;");
            statusLabel.setText("Неверный логин или пароль.");
        }
    }

    @FXML
    private void handleRegister() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.length() < 3 || pass.length() < 4) {
            statusLabel.setText("Слишком короткий логин/пароль!");
            return;
        }

        if (userRepository.register(user, pass)) {
            statusLabel.setStyle("-fx-text-fill: #a6e3a1;");
            statusLabel.setText("Регистрация успешна! Войдите.");
        } else {
            statusLabel.setStyle("-fx-text-fill: #f38ba8;");
            statusLabel.setText("Ошибка: имя пользователя занято.");
        }
    }

    private void openProfile(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store.fxml")); // сразу в магазин
            Scene scene = new Scene(loader.load());

            StoreController storeController = loader.getController();
            storeController.setCurrentUser(user);

            // Применяем тему
            String themeFile = user.getTheme().equals("DARK") ? "dark.css" : "light.css";
            scene.getStylesheets().add(getClass().getResource("/styles/" + themeFile).toExternalForm());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}