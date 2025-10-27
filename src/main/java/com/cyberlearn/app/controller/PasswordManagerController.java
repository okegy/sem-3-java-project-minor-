package com.cyberlearn.app.controller;

import com.cyberlearn.app.model.PasswordEntry;
import com.cyberlearn.app.util.Vault;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public class PasswordManagerController {
    @FXML private javafx.scene.control.ProgressBar strengthBar;
    @FXML private TextField txtSite;
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private TextField txtNote;
    @FXML private PasswordField txtMaster;
    @FXML private TableView<PasswordEntry> table;
    @FXML private TableColumn<PasswordEntry, String> colSite;
    @FXML private TableColumn<PasswordEntry, String> colUser;
    @FXML private TableColumn<PasswordEntry, String> colPass;
    @FXML private TableColumn<PasswordEntry, String> colNote;
    @FXML private Label lblStatus;

    private ObservableList<PasswordEntry> items = FXCollections.observableArrayList();
    private File vaultFile;

    @FXML
    public void initialize_old__remove(){
        colSite.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSite()));
        colUser.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));
        colPass.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty("●●●●●"));
        colNote.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNote()));
        table.setItems(items);
    }

    @FXML
    public void onAdd(){
        items.add(new PasswordEntry(txtSite.getText(), txtUser.getText(), txtPass.getText(), txtNote.getText()));
        try{var p=com.cyberlearn.app.util.ProgressService.get(com.cyberlearn.app.util.AuthService.getCurrent().getUsername()); p.setPassMgrUses(p.getPassMgrUses()+1); com.cyberlearn.app.util.ProgressService.update(p);}catch(Exception ex){}
        txtSite.clear(); txtUser.clear(); txtPass.clear(); txtNote.clear();
    }

    @FXML
    public void onSave(){
        if (vaultFile == null) {
            FileChooser fc = new FileChooser();
            fc.setInitialFileName("vault.clearn");
            vaultFile = fc.showSaveDialog(null);
        }
        try {
            com.cyberlearn.app.util.Vault.saveVault(vaultFile.toPath(), txtMaster.getText(), items);
            lblStatus.setText("Saved ✔ " + vaultFile.getName());
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void onOpen(){
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if (f == null) return;
        try {
            var list = com.cyberlearn.app.util.Vault.loadVault(f.toPath(), txtMaster.getText());
            items.setAll(list);
            vaultFile = f;
            lblStatus.setText("Opened ✔ " + f.getName());
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }

    private double strength(String s){
        if (s == null) return 0;
        int len = s.length();
        int classes = 0;
        if (s.matches(".*[a-z].*")) classes++;
        if (s.matches(".*[A-Z].*")) classes++;
        if (s.matches(".*\\d.*")) classes++;
        if (s.matches(".*[^a-zA-Z0-9].*")) classes++;
        double score = Math.min(1.0, (len/12.0)*0.6 + (classes/4.0)*0.4);
        return score;
    }

    @FXML
    public void onGenerate(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{};:,.?/";
        StringBuilder sb = new StringBuilder();
        java.util.Random r = new java.util.Random();
        for (int i=0;i<16;i++){ sb.append(chars.charAt(r.nextInt(chars.length()))); }
        txtPass.setText(sb.toString());
        if (strengthBar != null) strengthBar.setProgress(strength(sb.toString()));
    }
}
