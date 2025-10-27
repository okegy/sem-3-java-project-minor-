package com.cyberlearn.app.controller;

import com.cyberlearn.app.util.ProgressService;
import com.cyberlearn.app.util.AuthService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.Scene;

public class MainController {
    // Top bar
    @FXML private Label lblRank;
    @FXML private ProgressBar xpBar;
    @FXML private ScrollPane guideSidebar;
    
    // Dashboard - Profile Section
    @FXML private Label lblWelcome;
    @FXML private Label lblUsername;
    @FXML private Label lblRankDetail;
    @FXML private Label lblTotalXP;
    
    // Dashboard - Stats Section
    @FXML private Label lblQuizStats;
    @FXML private Label lblEncStats;
    @FXML private Label lblScanStats;
    @FXML private Label lblHashStats;
    @FXML private Label lblPassStats;
    
    // Dashboard - Badges
    @FXML private Label lblBadges;

    public void initialize() {
        try{
            String username = AuthService.getCurrent().getUsername();
            var p = ProgressService.get(username);
            
            // Calculate XP
            int xp = p.getEncUses()*10 + p.getScanUses()*10 + p.getHashUses()*10 + 
                     p.getPassMgrUses()*10 + p.getQuizScore()*5;
            
            // Determine rank based on XP
            String rank = "🥉 Explorer";
            if (xp >= 500) rank = "🥇 Cyber Master";
            else if (xp >= 300) rank = "🥈 Security Expert";
            else if (xp >= 150) rank = "🥉 Defender";
            
            // Update top bar
            lblRank.setText(rank.substring(3)); // Remove emoji for top bar
            double pct = Math.min(1.0, xp/500.0);
            xpBar.setProgress(pct);
            
            // Update profile section if elements exist
            if (lblWelcome != null) lblWelcome.setText("Welcome back, " + username + "! 👋");
            if (lblUsername != null) lblUsername.setText(username);
            if (lblRankDetail != null) lblRankDetail.setText(rank);
            if (lblTotalXP != null) lblTotalXP.setText(xp + " XP");
            
            // Update stats section if elements exist
            if (lblQuizStats != null) 
                lblQuizStats.setText("Score: " + p.getQuizScore() + " | Attempts: " + p.getQuizAttempts());
            if (lblEncStats != null)
                lblEncStats.setText(p.getEncUses() + " times" + (p.getEncUses() >= 5 ? " 🔥" : ""));
            if (lblScanStats != null)
                lblScanStats.setText(p.getScanUses() + " scans" + (p.getScanUses() >= 5 ? " 🔥" : ""));
            if (lblHashStats != null)
                lblHashStats.setText(p.getHashUses() + " files" + (p.getHashUses() >= 5 ? " 🔥" : ""));
            if (lblPassStats != null)
                lblPassStats.setText(p.getPassMgrUses() + " uses" + (p.getPassMgrUses() >= 5 ? " 🔥" : ""));
            
            // Update badges
            if (lblBadges != null) {
                StringBuilder badges = new StringBuilder();
                if (p.getEncUses() >= 1) badges.append("🔐 Crypto Wizard | ");
                if (p.getScanUses() >= 1) badges.append("📡 Port Protector | ");
                if (p.getHashUses() >= 1) badges.append("📁 File Guardian | ");
                if (p.getPassMgrUses() >= 1) badges.append("🔑 Password Pro | ");
                if (p.getQuizScore() >= 5) badges.append("🧠 Quiz Master | ");
                if (p.getEncUses() >= 5) badges.append("⚡ Encryption Expert | ");
                if (xp >= 100) badges.append("⭐ Rising Star | ");
                if (xp >= 300) badges.append("🏆 Elite Hacker | ");
                
                if (badges.length() > 0) {
                    badges.setLength(badges.length() - 3); // Remove last " | "
                    lblBadges.setText(badges.toString());
                } else {
                    lblBadges.setText("Complete tasks to earn your first badge! Start by using the tools above. 🚀");
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void onToggleTheme(){
        Scene scene = ((Node)lblRank.getScene().getRoot()).getScene();
        com.cyberlearn.app.ThemeManager.toggle(scene);
    }
    
    @FXML
    public void onGoToEncryption(){
        navigateToTab(1); // Encryption tab
    }
    
    @FXML
    public void onGoToPortScanner(){
        navigateToTab(2); // Port Scanner tab
    }
    
    @FXML
    public void onGoToIntegrity(){
        navigateToTab(3); // Integrity tab
    }
    
    @FXML
    public void onGoToPasswords(){
        navigateToTab(4); // Passwords tab
    }
    
    private void navigateToTab(int tabIndex){
        try {
            javafx.scene.control.TabPane tabPane = (javafx.scene.control.TabPane) lblRank.getScene().lookup(".tab-pane");
            if (tabPane != null && tabIndex < tabPane.getTabs().size()) {
                tabPane.getSelectionModel().select(tabIndex);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onToggleGuide(){
        if (guideSidebar != null) {
            guideSidebar.setVisible(!guideSidebar.isVisible());
        }
    }
    
    @FXML
    public void onShowProfile(){
        try {
            var user = AuthService.getCurrent();
            var progress = ProgressService.get(user.getUsername());
            
            // Calculate XP and rank
            int xp = progress.getEncUses()*10 + progress.getScanUses()*10 + 
                     progress.getHashUses()*10 + progress.getPassMgrUses()*10 + 
                     progress.getQuizScore()*5;
            
            String rank = "Explorer";
            if (xp >= 500) rank = "Cyber Master";
            else if (xp >= 300) rank = "Security Expert";
            else if (xp >= 150) rank = "Defender";
            
            // Create profile dialog
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("User Profile");
            dialog.setHeaderText("CyberLearn Profile");
            
            // Create content
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            grid.add(new Label("Username:"), 0, 0);
            grid.add(new Label(user.getUsername()), 1, 0);
            
            grid.add(new Label("Role:"), 0, 1);
            grid.add(new Label(user.getRole()), 1, 1);
            
            grid.add(new Label("Current Rank:"), 0, 2);
            grid.add(new Label(rank), 1, 2);
            
            grid.add(new Label("Total XP:"), 0, 3);
            grid.add(new Label(xp + " XP"), 1, 3);
            
            grid.add(new Label("XP to Next Rank:"), 0, 4);
            int nextRank = xp < 150 ? 150 : xp < 300 ? 300 : xp < 500 ? 500 : 500;
            int xpNeeded = Math.max(0, nextRank - xp);
            grid.add(new Label(xpNeeded + " XP"), 1, 4);
            
            grid.add(new Separator(), 0, 5, 2, 1);
            
            grid.add(new Label("Statistics:"), 0, 6);
            grid.add(new Label(""), 1, 6);
            
            grid.add(new Label("Quiz Score:"), 0, 7);
            grid.add(new Label(progress.getQuizScore() + " points"), 1, 7);
            
            grid.add(new Label("Quiz Attempts:"), 0, 8);
            grid.add(new Label(String.valueOf(progress.getQuizAttempts())), 1, 8);
            
            grid.add(new Label("Encryption Uses:"), 0, 9);
            grid.add(new Label(String.valueOf(progress.getEncUses())), 1, 9);
            
            grid.add(new Label("Port Scans:"), 0, 10);
            grid.add(new Label(String.valueOf(progress.getScanUses())), 1, 10);
            
            grid.add(new Label("File Checks:"), 0, 11);
            grid.add(new Label(String.valueOf(progress.getHashUses())), 1, 11);
            
            grid.add(new Label("Password Manager:"), 0, 12);
            grid.add(new Label(String.valueOf(progress.getPassMgrUses())), 1, 12);
            
            grid.add(new Label("Lessons Completed:"), 0, 13);
            grid.add(new Label(progress.getCompletedLessons().size() + "/52"), 1, 13);
            
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            
            dialog.showAndWait();
            
        } catch(Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load profile");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
