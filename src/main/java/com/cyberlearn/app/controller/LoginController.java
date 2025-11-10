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
            // Validate input
            String username = txtUser.getText().trim();
            String password = txtPass.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                showError("Please enter both username and password");
                return;
            }
            
            // Show loading state
            setLoading(true);
            lblStatus.getStyleClass().remove("error");
            lblStatus.setText("Logging in...");
            
            // Run login in background thread to keep UI responsive
            new Thread(() -> {
                try {
                    boolean loginSuccess = AuthService.login(username, password);
                    
                    if (!loginSuccess) {
                        javafx.application.Platform.runLater(() -> {
                            showError("Invalid username or password");
                            txtPass.clear();
                            txtPass.requestFocus();
                        });
                        return;
                    }
                    
                    // Initialize user progress if needed
                    try {
                        com.cyberlearn.app.util.ProgressService.get(username);
                    } catch (Exception e) {
                        System.out.println("Initializing progress for user: " + username);
                    }
                    
                    // Navigate to appropriate screen
                    javafx.application.Platform.runLater(() -> {
                        lblStatus.setText("Login successful! Loading...");
                        if ("ADMIN".equalsIgnoreCase(AuthService.getCurrent().getRole())) {
                            Navigator.go("admin.fxml");
                        } else {
                            Navigator.go("main.fxml");
                        }
                    });
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    javafx.application.Platform.runLater(() -> {
                        showError("Login failed: " + e.getMessage());
                    });
                } finally {
                    javafx.application.Platform.runLater(() -> setLoading(false));
                }
            }).start();
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("An unexpected error occurred");
            setLoading(false);
        }
    }
    
    private void showError(String message) {
        lblStatus.getStyleClass().add("error");
        lblStatus.setText(message);
    }
    
    private void setLoading(boolean loading) {
        btnLogin.setDisable(loading);
        btnRegister.setDisable(loading);
        txtUser.setDisable(loading);
        txtPass.setDisable(loading);
    }

    @FXML
    public void onRegister(){
        Navigator.go("register.fxml");
    }
}
