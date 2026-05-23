package com.store.app.controllers;

import com.store.app.models.Product;
import com.store.app.models.User;
import com.store.app.repositories.ProductRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class StoreController {

    @FXML private FlowPane gamesContainer;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private ComboBox<String> categoryComboBox;

    private final ProductRepository productRepository = new ProductRepository();
    private User currentUser;

    @FXML
    public void initialize() {
        setupFilters();
        loadAllProducts();
        searchField.textProperty().addListener((obs, old, newText) -> filterProducts());
    }

    private void setupFilters() {
        sortComboBox.getItems().addAll("По умолчанию", "По цене ↑", "По цене ↓", "По названию", "По рейтингу");
        sortComboBox.setValue("По умолчанию");

        categoryComboBox.getItems().addAll("Все", "GAME", "DLC", "OTHER");
        categoryComboBox.setValue("Все");

        sortComboBox.setOnAction(e -> filterProducts());
        categoryComboBox.setOnAction(e -> filterProducts());
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private void loadAllProducts() {
        gamesContainer.getChildren().clear();
        for (Product p : productRepository.getAllProducts()) {
            gamesContainer.getChildren().add(createProductCard(p));
        }
    }

    private void filterProducts() {
        String query = searchField.getText().trim().toLowerCase();
        String sort = getSortValue();
        String category = getCategoryValue();

        gamesContainer.getChildren().clear();

        for (Product p : productRepository.getAllProducts(sort, category)) {
            if (query.isEmpty() || p.getTitle().toLowerCase().contains(query)) {
                gamesContainer.getChildren().add(createProductCard(p));
            }
        }
    }

    private String getSortValue() {
        return switch (sortComboBox.getValue()) {
            case "По цене ↑" -> "price_asc";
            case "По цене ↓" -> "price_desc";
            case "По названию" -> "name";
            case "По рейтингу" -> "rating";
            default -> "default";
        };
    }

    private String getCategoryValue() {
        return switch (categoryComboBox.getValue()) {
            case "GAME" -> "GAME";
            case "DLC" -> "DLC";
            case "OTHER" -> "OTHER";
            default -> "ALL";
        };
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.getStyleClass().add("game-card");

        Label title = new Label(product.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 15.5px; -fx-text-fill: white; -fx-wrap-text: true;");

        String priceText = (product.getPrice() == 0) ? "БЕСПЛАТНО" : formatPrice(product.getPrice());
        Label price = new Label(priceText);
        price.setStyle("-fx-text-fill: #a6e3a1; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label rating = new Label(product.getRatingStars());
        rating.setStyle("-fx-text-fill: #f9e076; -fx-font-size: 13px;");

        card.getChildren().addAll(title, price, rating);
        card.setOnMouseClicked(e -> openProductDetail(product));

        return card;
    }

    private String formatPrice(double price) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        return df.format(price).replace(",", " ") + " ₽";
    }

    private void openProductDetail(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/productDetail.fxml"));
            Parent root = loader.load();
            ProductDetailController ctrl = loader.getController();
            ctrl.setProduct(product, currentUser);

            Stage stage = (Stage) gamesContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAllProducts() {
        searchField.clear();
        sortComboBox.setValue("По умолчанию");
        categoryComboBox.setValue("Все");
        loadAllProducts();
    }

    @FXML
    private void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
            Parent root = loader.load();
            ProfileController ctrl = loader.getController();
            ctrl.setUserData(currentUser);

            Stage stage = (Stage) gamesContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openLibrary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/library.fxml"));
            Parent root = loader.load();
            LibraryController ctrl = loader.getController();
            ctrl.setCurrentUser(currentUser);

            Stage stage = (Stage) gamesContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}