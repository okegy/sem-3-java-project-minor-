package com.cyberlearn.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Navigator {
    private static Stage stage;

    public static void init(Stage primary) {
        stage = primary;
    }

    public static void go(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigator.class.getResource("/com/cyberlearn/app/fxml/" + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(Navigator.class.getResource("/com/cyberlearn/app/css/cyberpunk.css").toExternalForm());
            stage.setScene(scene);
            if (!stage.isMaximized()) {
                stage.setMaximized(true); // Keep maximized when navigating
            }
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + fxml);
            e.printStackTrace();
            // Surface error to UI so users aren't stuck on Loading...
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Navigation Error");
                alert.setHeaderText("Could not load screen: " + fxml);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
        }
    }
}
