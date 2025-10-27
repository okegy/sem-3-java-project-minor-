package com.cyberlearn.app.controller;

import com.cyberlearn.app.Navigator;
import com.cyberlearn.app.model.Progress;
import com.cyberlearn.app.model.User;
import com.cyberlearn.app.util.AuthService;
import com.cyberlearn.app.util.ProgressService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;

import javafx.scene.Scene;

public class AdminController {

    @FXML private TableView<User> tblUsers;
    @FXML private TableColumn<User, String> colUser;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableView<Progress> tblProgress;
    @FXML private TableColumn<Progress, String> colPUser;
    @FXML private TableColumn<Progress, Number> colQuiz;
    @FXML private TableColumn<Progress, Number> colAttempts;
    @FXML private TableColumn<Progress, Number> colEnc;
    @FXML private TableColumn<Progress, Number> colScan;
    @FXML private TableColumn<Progress, Number> colHash;
    @FXML private TableColumn<Progress, Number> colPass;
    @FXML private Label lblStatus;
    @FXML private Button btnDummy;

    @FXML
    public void initialize(){
        colUser.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));
        colRole.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRole()));
        colPUser.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));
        colQuiz.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuizScore()));
        colAttempts.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuizAttempts()));
        colEnc.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getEncUses()));
        colScan.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getScanUses()));
        colHash.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getHashUses()));
        colPass.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPassMgrUses()));

        try {
            ObservableList<User> users = FXCollections.observableArrayList(AuthService.read());
            tblUsers.setItems(users);
            ObservableList<Progress> progress = FXCollections.observableArrayList(ProgressService.all());
            tblProgress.setItems(progress);
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void onExportCSV(){
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Export All Students Progress to CSV");
            fc.setInitialFileName("all_students_progress.csv");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File f = fc.showSaveDialog(null);
            if (f == null) return;
            
            int count = 0;
            try (FileWriter w = new FileWriter(f)) {
                w.write("username,quizScore,quizAttempts,encUses,scanUses,hashUses,passMgrUses\n");
                for (Progress p : ProgressService.all()){
                    w.write(String.format("%s,%d,%d,%d,%d,%d,%d\n",
                            p.getUsername(), p.getQuizScore(), p.getQuizAttempts(),
                            p.getEncUses(), p.getScanUses(), p.getHashUses(), p.getPassMgrUses()));
                    count++;
                }
            }
            lblStatus.setText("✅ Exported " + count + " students to: " + f.getName());
        } catch (Exception e) {
            lblStatus.setText("❌ Error exporting CSV: " + e.getMessage());
        }
    }

    @FXML
    public void onGenerateReports(){
        try {
            File dir = new File("reports");
            if (!dir.exists()) dir.mkdirs();
            
            int count = 0;
            for (Progress p : ProgressService.all()){
                File out = new File(dir, p.getUsername() + "_report.txt");
                try (FileWriter w = new FileWriter(out)) {
                    w.write("═══════════════════════════════════════\n");
                    w.write("  CyberLearn Student Progress Report  \n");
                    w.write("═══════════════════════════════════════\n\n");
                    w.write("Student: " + p.getUsername() + "\n\n");
                    w.write("📝 Quiz Performance:\n");
                    w.write("   Score: " + p.getQuizScore() + "\n");
                    w.write("   Attempts: " + p.getQuizAttempts() + "\n\n");
                    w.write("🛠 Tool Usage:\n");
                    w.write("   Encryption: " + p.getEncUses() + " times\n");
                    w.write("   Port Scans: " + p.getScanUses() + " times\n");
                    w.write("   Hash Checks: " + p.getHashUses() + " times\n");
                    w.write("   Password Vault: " + p.getPassMgrUses() + " times\n\n");
                    w.write("Generated: " + java.time.LocalDateTime.now() + "\n");
                }
                count++;
            }
            lblStatus.setText("✅ Generated " + count + " reports in: " + dir.getAbsolutePath());
        } catch (Exception e) {
            lblStatus.setText("❌ Error generating reports: " + e.getMessage());
        }
    }

    @FXML
    public void onLogout(){
        Navigator.go("login.fxml");
    }

    @FXML
    public void onToggleTheme(){
        Scene scene = lblStatus.getScene();
        com.cyberlearn.app.ThemeManager.toggle(scene);
    }

    @FXML
    public void onExportSelectedCSV(){
        try {
            Progress p = tblProgress.getSelectionModel().getSelectedItem();
            if (p == null){ 
                lblStatus.setText("⚠️ Please select a student from the Progress table first"); 
                return; 
            }
            
            FileChooser fc = new FileChooser();
            fc.setTitle("Export " + p.getUsername() + "'s Progress to CSV");
            fc.setInitialFileName(p.getUsername() + "_progress.csv");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File f = fc.showSaveDialog(null);
            if (f == null) return;
            
            try (FileWriter w = new FileWriter(f)) {
                w.write("username,quizScore,quizAttempts,encUses,scanUses,hashUses,passMgrUses\n");
                w.write(String.format("%s,%d,%d,%d,%d,%d,%d\n",
                        p.getUsername(), p.getQuizScore(), p.getQuizAttempts(),
                        p.getEncUses(), p.getScanUses(), p.getHashUses(), p.getPassMgrUses()));
            }
            lblStatus.setText("✅ Exported " + p.getUsername() + "'s progress to: " + f.getName());
        } catch (Exception e) {
            lblStatus.setText("❌ Error exporting CSV: " + e.getMessage());
        }
    }

    @FXML
    public void onExportSelectedPDF(){
        try {
            Progress p = tblProgress.getSelectionModel().getSelectedItem();
            if (p == null){ 
                lblStatus.setText("⚠️ Please select a student from the Progress table first"); 
                return; 
            }
            
            FileChooser fc = new FileChooser();
            fc.setTitle("Export " + p.getUsername() + "'s Report to PDF");
            fc.setInitialFileName(p.getUsername() + "_progress_report.pdf");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File f = fc.showSaveDialog(null);
            if (f == null) return;

            // OpenPDF
            com.lowagie.text.Document doc = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(f));
            doc.open();
            
            // Fonts
            com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLD);
            com.lowagie.text.Font headFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.BOLD);
            com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12);
            
            // Content
            doc.add(new com.lowagie.text.Paragraph("CyberLearn Student Progress Report", titleFont));
            doc.add(new com.lowagie.text.Paragraph(" ")); // Space
            doc.add(new com.lowagie.text.Paragraph("Student: " + p.getUsername(), headFont));
            doc.add(new com.lowagie.text.Paragraph("Generated: " + java.time.LocalDateTime.now(), normalFont));
            doc.add(new com.lowagie.text.Paragraph(" "));
            
            doc.add(new com.lowagie.text.Paragraph("Quiz Performance", headFont));
            doc.add(new com.lowagie.text.Paragraph("  Score: " + p.getQuizScore(), normalFont));
            doc.add(new com.lowagie.text.Paragraph("  Attempts: " + p.getQuizAttempts(), normalFont));
            doc.add(new com.lowagie.text.Paragraph(" "));
            
            doc.add(new com.lowagie.text.Paragraph("Tool Usage Statistics", headFont));
            doc.add(new com.lowagie.text.Paragraph("  Encryption Tool: " + p.getEncUses() + " times", normalFont));
            doc.add(new com.lowagie.text.Paragraph("  Port Scanner: " + p.getScanUses() + " times", normalFont));
            doc.add(new com.lowagie.text.Paragraph("  Hash/Integrity Checker: " + p.getHashUses() + " times", normalFont));
            doc.add(new com.lowagie.text.Paragraph("  Password Vault Manager: " + p.getPassMgrUses() + " times", normalFont));
            
            doc.close();
            lblStatus.setText("✅ Exported " + p.getUsername() + "'s PDF report to: " + f.getName());
        } catch (Exception e) {
            lblStatus.setText("❌ Error exporting PDF: " + e.getMessage());
        }
    }


    @FXML
    public void onManageMaterials(){
        com.cyberlearn.app.Navigator.go("admin_materials.fxml");
    }


    @FXML
    public void onResetPassword(){
        try{
            User u = tblUsers.getSelectionModel().getSelectedItem();
            if (u==null){ 
                lblStatus.setText("⚠️ Please select a user from the Users table first"); 
                return; 
            }
            
            TextInputDialog d = new TextInputDialog();
            d.setTitle("Reset Password");
            d.setHeaderText("Reset password for user: " + u.getUsername());
            d.setContentText("Enter new password:");
            var res = d.showAndWait();
            if (res.isEmpty() || res.get().isBlank()) {
                lblStatus.setText("❌ Password reset cancelled");
                return;
            }
            
            com.cyberlearn.app.util.AuthService.resetPassword(u.getUsername(), res.get());
            tblUsers.setItems(FXCollections.observableArrayList(com.cyberlearn.app.util.AuthService.read()));
            lblStatus.setText("✅ Password reset successfully for: " + u.getUsername());
        }catch(Exception e){ 
            lblStatus.setText("❌ Error resetting password: " + e.getMessage()); 
        }
    }

    @FXML
    public void onToggleActive(){
        try{
            User u = tblUsers.getSelectionModel().getSelectedItem();
            if (u==null){ 
                lblStatus.setText("⚠️ Please select a user from the Users table first"); 
                return; 
            }
            
            if ("ADMIN".equalsIgnoreCase(u.getRole()) && "admin".equalsIgnoreCase(u.getUsername())) {
                lblStatus.setText("❌ Cannot deactivate the main admin account");
                return;
            }
            
            boolean active = com.cyberlearn.app.util.AuthService.toggleActive(u.getUsername());
            tblUsers.setItems(FXCollections.observableArrayList(com.cyberlearn.app.util.AuthService.read()));
            String status = active ? "✅ Activated" : "⚠️ Deactivated";
            lblStatus.setText(status + " user: " + u.getUsername());
        }catch(Exception e){ 
            lblStatus.setText("❌ Error toggling user status: " + e.getMessage()); 
        }
    }
}
