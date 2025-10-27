package com.cyberlearn.app.controller;

import com.cyberlearn.app.Navigator;
import com.cyberlearn.app.util.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private PasswordField txtPass2;
    @FXML private Label lblStatus;

    @FXML
    public void onCreate(){
        try {
            if (txtUser.getText().isBlank() || txtPass.getText().isBlank()) {
                lblStatus.setText("Please fill all fields");
                return;
            }
            if (!txtPass.getText().equals(txtPass2.getText())){
                lblStatus.setText("Passwords do not match");
                return;
            }
            boolean ok = AuthService.register(txtUser.getText(), txtPass.getText());
            if (!ok) { lblStatus.setText("Username already exists"); return; }
            
            // Initialize progress for new user
            try {
                com.cyberlearn.app.util.ProgressService.get(txtUser.getText());
            } catch (Exception ignored) {}
            
            lblStatus.setText("Account created ✔ Redirecting to login...");
            // Auto-navigate back to login after 1.5 seconds
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            pause.setOnFinished(e -> Navigator.go("login.fxml"));
            pause.play();
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void onBack(){
        Navigator.go("login.fxml");
    }
}
