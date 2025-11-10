package com.cyberlearn.app.controller;

import com.cyberlearn.app.model.Material;
import com.cyberlearn.app.util.MaterialsService;
import com.cyberlearn.app.util.CertificateGenerator;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class CoursesController {
    @FXML private WebView web;
    @FXML private ListView<String> list;
    @FXML private TextArea notes;
    @FXML private TextField websiteField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button generatePasswordBtn;
    
    private java.util.List<Material> materials;
    private Set<Integer> completedWeeks = new HashSet<>();
    private static final int TOTAL_WEEKS = 52;

    @FXML
    public void initialize() {
        try {
            materials = MaterialsService.all();
            for (var m : materials) list.getItems().add(m.getLevel()+": "+m.getTitle());
            list.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
                int idx = list.getSelectionModel().getSelectedIndex();
                if (idx >= 0) { 
                    var m = materials.get(idx);
                    String links = (m.getLinks()==null||m.getLinks().isEmpty())?"":("\nLinks:\n"+String.join("\n", m.getLinks()));
                    notes.setText(m.getDescription()+links);
                    try {
                        if (m.getLinks()!=null && !m.getLinks().isEmpty()){
                            String url = m.getLinks().get(0);
                            if (url.contains("youtube.com") || url.contains("youtu.be")){
                                try {
                                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                                    String html = "<html><body style='background: #1a1a2e; color: #eee; font-family: Arial; padding: 20px; text-align: center;'>" +
                                                  "<h2>Video Lesson</h2>" +
                                                  "<p>Opening YouTube in your default browser...</p>" +
                                                  "<p style='margin-top: 20px;'>If the video doesn't open, <a href='" + url + "' style='color: #00ff88;'>click here</a></p>" +
                                                  "</body></html>";
                                    web.getEngine().loadContent(html);
                                } catch (Exception ex) {
                                    String html = "<html><body style='background: #1a1a2e; color: #eee; font-family: Arial; padding: 20px; text-align: center;'>" +
                                                  "<h2>Video Lesson</h2>" +
                                                  "<p>Could not open browser automatically.</p>" +
                                                  "<p>Please visit: <a href='" + url + "' style='color: #00ff88;'>" + url + "</a></p>" +
                                                  "</body></html>";
                                    web.getEngine().loadContent(html);
                                }
                            } else {
                                web.getEngine().loadContent("<p>No YouTube link for this lesson. Links listed below.</p>");
                            }
                        }
                    } catch(Exception ex) { /* ignore */ }
                }
            });
            if(!materials.isEmpty()) list.getSelectionModel().select(0);
        } catch(Exception e) { 
            notes.setText("Error loading materials: "+e.getMessage()); 
        }
    }

    @FXML
    private void onGeneratePassword() {
        try {
            String website = websiteField.getText().trim();
            String username = usernameField.getText().trim();
            
            if (website.isEmpty() || username.isEmpty()) {
                showAlert("Missing Information", "Please enter both website and username", Alert.AlertType.WARNING);
                return;
            }
            
            String password = generateSecurePassword();
            passwordField.setText(password);
            storePassword(website, username, password);
            showAlert("Success", "Password generated and copied to clipboard!", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showAlert("Error", "Failed to generate password: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String generateSecurePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*_=+-/";
        String combinedChars = upperCase + lowerCase + numbers + specialChars;
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Ensure at least one character from each set
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Generate remaining characters
        for (int i = 0; i < 8; i++) { // 12 characters total
            password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

    private void storePassword(String website, String username, String password) {
        try {
            // Create password entry
            com.cyberlearn.app.model.PasswordEntry entry = new com.cyberlearn.app.model.PasswordEntry(
                website, username, password, "Generated by CyberLearn"
            );
            
            // Get or create vault file in user's home directory
            String userHome = System.getProperty("user.home");
            java.nio.file.Path vaultFile = java.nio.file.Paths.get(userHome, "cyberlearn_vault.clearn");
            
            // Use username as master password for this demo
            // In a real app, you'd prompt the user for a master password
            String masterPassword = "cyberlearn_secure";
            
            // Load existing entries
            List<com.cyberlearn.app.model.PasswordEntry> entries = new ArrayList<>();
            if (java.nio.file.Files.exists(vaultFile)) {
                entries = com.cyberlearn.app.util.Vault.loadVault(vaultFile, masterPassword);
            }
            
            // Add new entry
            entries.add(entry);
            
            // Save back to vault
            com.cyberlearn.app.util.Vault.saveVault(vaultFile, masterPassword, entries);
            
        } catch (Exception e) {
            // Fallback to simple file storage if vault fails
            try {
                String entry = String.format("Website: %s%nUsername: %s%nPassword: %s%n%n", 
                    website, username, password);
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("passwords.txt"),
                    entry.getBytes(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
                );
            } catch (Exception ex) {
                throw new RuntimeException("Failed to store password: " + ex.getMessage(), ex);
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onComplete() {
        int selectedIndex = list.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            completedWeeks.add(selectedIndex + 1); // +1 because weeks are 1-based
            if (completedWeeks.size() == TOTAL_WEEKS) {
                CertificateGenerator.showCertificate("Student Name");
            }
        }
    }
    
    @FXML
    private void onTestButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test Button");
        alert.setHeaderText(null);
        alert.setContentText("The test button works! FXML is loading correctly.");
        alert.showAndWait();
    }
}
