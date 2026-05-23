package com.store.app.controllers;

import com.store.app.models.Product;
import com.store.app.models.Review;
import com.store.app.models.User;
import com.store.app.repositories.ReviewRepository;
import com.store.app.repositories.PurchaseRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ProductDetailController {

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private TextArea descriptionArea;
    @FXML private VBox reviewsContainer;
    @FXML private ComboBox<Integer> ratingBox;
    @FXML private TextField commentField;
    @FXML private Button buyButton;

    private Product currentProduct;
    private User currentUser;

    private final ReviewRepository reviewRepo = new ReviewRepository();
    private final PurchaseRepository purchaseRepo = new PurchaseRepository();

    public void setProduct(Product product, User user) {
        this.currentProduct = product;
        this.currentUser = user;   // ← Теперь сохраняем пользователя

        if (product == null) return;

        titleLabel.setText(product.getTitle());
        priceLabel.setText(product.getPrice() == 0 ? "БЕСПЛАТНО" : product.getPrice() + " ₽");
        descriptionArea.setText(product.getDescription() != null ? product.getDescription() : "Описание отсутствует.");

        // Заполняем рейтинг
        ratingBox.getItems().setAll(1, 2, 3, 4, 5);

        loadReviews();

        // Проверяем покупку
        if (currentUser != null && purchaseRepo.isPurchased(currentUser.getId(), product.getId())) {
            buyButton.setText("Уже куплено");
            buyButton.setDisable(true);
        }
    }

    private void loadReviews() {
        reviewsContainer.getChildren().clear();
        if (currentProduct == null) return;

        List<Review> reviews = reviewRepo.getReviewsByProduct(currentProduct.getId());

        for (Review r : reviews) {
            VBox box = new VBox(5);
            Label ratingLabel = new Label("★".repeat(r.getRating()) + "   " + r.getUsername());
            ratingLabel.setStyle("-fx-text-fill: #f9e076; -fx-font-weight: bold;");
            Label commentLabel = new Label(r.getComment());
            commentLabel.setStyle("-fx-text-fill: white; -fx-wrap-text: true;");

            box.getChildren().addAll(ratingLabel, commentLabel);
            reviewsContainer.getChildren().add(box);
        }

        if (reviews.isEmpty()) {
            reviewsContainer.getChildren().add(new Label("Пока нет отзывов."));
        }
    }

    @FXML
    private void handleAddReview() {
        if (currentUser == null) {
            System.err.println("Ошибка: пользователь не авторизован");
            return;
        }
        if (ratingBox.getValue() == null || commentField.getText().trim().isEmpty()) {
            System.err.println("Заполните рейтинг и комментарий");
            return;
        }

        reviewRepo.addReview(
                currentUser.getId(),
                currentProduct.getId(),
                ratingBox.getValue(),
                commentField.getText().trim()
        );

        commentField.clear();
        ratingBox.setValue(null);
        loadReviews(); // обновляем список отзывов
    }

    @FXML
    private void handleBuy() {
        if (currentUser == null) {
            System.err.println("Ошибка: пользователь не авторизован");
            return;
        }

        purchaseRepo.addPurchase(currentUser.getId(), currentProduct.getId());
        buyButton.setText("Куплено!");
        buyButton.setDisable(true);
    }

    @FXML
    private void goBackToStore() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/store.fxml"));
            Parent root = loader.load();

            StoreController storeCtrl = loader.getController();
            storeCtrl.setCurrentUser(currentUser);

            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}