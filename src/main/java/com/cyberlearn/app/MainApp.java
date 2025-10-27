package com.cyberlearn.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Navigator.init(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cyberlearn/app/fxml/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/com/cyberlearn/app/css/cyberpunk.css").toExternalForm());
        stage.setTitle("CyberLearn — Learn. Play. Secure.");
        stage.setMaximized(true); // Start maximized
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
