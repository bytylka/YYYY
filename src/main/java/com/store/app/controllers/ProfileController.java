package com.store.app.controllers;

import com.store.app.models.User;
import com.store.app.repositories.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class ProfileController {

    @FXML private TextField nameField;
    @FXML private TextArea aboutArea;
    @FXML private ComboBox<String> themeBox;
    @FXML private ImageView avatarView;

    private User sessionUser;
    private final UserRepository repo = new UserRepository();

    @FXML
    public void initialize() {
        themeBox.getItems().addAll("LIGHT", "DARK");
    }

    public void setUserData(User user) {
        this.sessionUser = user;
        if (user == null) return;

        nameField.setText(user.getUsername());
        aboutArea.setText(user.getAbout() != null ? user.getAbout() : "");
        themeBox.setValue(user.getTheme() != null ? user.getTheme() : "DARK");
    }

    @FXML
    private void handleSave() {
        if (sessionUser == null) return;

        sessionUser.setUsername(nameField.getText());
        sessionUser.setAbout(aboutArea.getText());
        sessionUser.setTheme(themeBox.getValue());

        repo.saveProfile(sessionUser);

        // Применяем тему сразу ко всей сцене
        applyThemeToScene();
        System.out.println("Профиль сохранён. Тема: " + sessionUser.getTheme());
    }

    private void applyThemeToScene() {
        if (sessionUser == null) return;

        String themeFile = "DARK".equals(sessionUser.getTheme()) ? "dark.css" : "light.css";
        Scene scene = nameField.getScene();

        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/styles/" + themeFile).toExternalForm());
        }
    }

    @FXML
    private void goToStore() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store.fxml"));
            Parent root = loader.load();

            StoreController storeCtrl = loader.getController();
            storeCtrl.setCurrentUser(sessionUser);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(root);

            // Применяем тёмную тему по умолчанию при выходе
            scene.getStylesheets().add(getClass().getResource("/styles/dark.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("xPlay - Store");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}