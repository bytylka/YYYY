package com.store.app.controllers;

import com.store.app.models.Product;
import com.store.app.models.User;
import com.store.app.repositories.ProductRepository;
import com.store.app.repositories.PurchaseRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class LibraryController {

    @FXML private FlowPane libraryContainer;

    private User currentUser;
    private final PurchaseRepository purchaseRepository = new PurchaseRepository();
    private final ProductRepository productRepository = new ProductRepository();

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadLibrary();
    }

    private void loadLibrary() {
        libraryContainer.getChildren().clear();

        if (currentUser == null) {
            libraryContainer.getChildren().add(new Label("Пользователь не авторизован"));
            return;
        }

        List<Product> purchased = purchaseRepository.getPurchasedProducts(currentUser.getId());

        if (purchased.isEmpty()) {
            Label empty = new Label("Ваша библиотека пока пуста.\nКупите игры в магазине.");
            empty.setStyle("-fx-text-fill: #888; -fx-font-size: 15px;");
            libraryContainer.getChildren().add(empty);
            return;
        }

        for (Product p : purchased) {
            libraryContainer.getChildren().add(createLibraryCard(p));
        }
    }

    private VBox createLibraryCard(Product product) {
        VBox card = new VBox(10);
        card.getStyleClass().add("game-card");

        Label title = new Label(product.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");

        Label status = new Label("✓ Куплено");
        status.setStyle("-fx-text-fill: #a6e3a1; -fx-font-size: 14px; -fx-font-weight: bold;");

        card.getChildren().addAll(title, status);
        card.setOnMouseClicked(e -> openProductDetail(product));

        return card;
    }

    private void openProductDetail(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/productDetail.fxml"));
            Parent root = loader.load();
            ProductDetailController ctrl = loader.getController();
            ctrl.setProduct(product, currentUser);

            Stage stage = (Stage) libraryContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToStore() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store.fxml"));
            Parent root = loader.load();
            StoreController ctrl = loader.getController();
            ctrl.setCurrentUser(currentUser);

            Stage stage = (Stage) libraryContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
            Parent root = loader.load();
            ProfileController ctrl = loader.getController();
            ctrl.setUserData(currentUser);

            Stage stage = (Stage) libraryContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}