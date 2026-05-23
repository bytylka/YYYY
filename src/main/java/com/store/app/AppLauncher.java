package com.store.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class AppLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/fxml/login.fxml");

        if (fxmlLocation == null) {
            throw new RuntimeException("Критическая ошибка: Файл /fxml/login.fxml не найден в ресурсах JAR!");
        }

        Parent root = FXMLLoader.load(fxmlLocation);
        Scene scene = new Scene(root);

        // Проверка CSS (необязательно, но полезно)
        URL cssLocation = getClass().getResource("/styles/dark.css");
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        }

        primaryStage.setTitle("xPlay - Store");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}