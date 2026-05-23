package com.store.app.utils;

import javafx.scene.Scene;

public class ThemeManager {
    public static void applyTheme(Scene scene, String theme) {
        scene.getStylesheets().clear();
        String path = "/styles/" + theme.toLowerCase() + ".css";
        String css = ThemeManager.class.getResource(path).toExternalForm();
        scene.getStylesheets().add(css);
    }
}