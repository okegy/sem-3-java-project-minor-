package com.cyberlearn.app.controller;

import com.cyberlearn.app.Navigator;
import com.cyberlearn.app.model.User;
import com.cyberlearn.app.util.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private Label lblStatus;
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;

    @FXML
    public void initialize(){
        try { AuthService.ensureAdmin(); } catch (Exception ignored) {}
    }

    @FXML
    public void onLogin(){
        try {
            if (txtUser.getText().isBlank() || txtPass.getText().isBlank()) {
                lblStatus.setText("Please enter username and password");
                return;
            }
            
            lblStatus.setText("Logging in...");
            boolean ok = AuthService.login(txtUser.getText(), txtPass.getText());
            
            if (!ok) { 
                lblStatus.setText("Invalid username or password");
                txtPass.clear();
                return; 
            }
            
            // Ensure progress exists for user
            try {
                com.cyberlearn.app.util.ProgressService.get(AuthService.getCurrent().getUsername());
            } catch (Exception ignored) {}
            
            lblStatus.setText("Login successful! Loading...");
            
            if ("ADMIN".equalsIgnoreCase(AuthService.getCurrent().getRole())) {
                Navigator.go("admin.fxml");
            } else {
                Navigator.go("main.fxml");
            }
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegister(){
        Navigator.go("register.fxml");
    }
}
