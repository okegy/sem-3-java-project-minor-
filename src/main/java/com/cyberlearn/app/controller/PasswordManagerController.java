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
    public void initialize(){
        colSite.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSite()));
        colUser.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));
        colPass.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPassword()));
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
            if (vaultFile == null) { lblStatus.setText("Save cancelled"); return; }
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
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String symbols = "!@#$%^&*()-_=+[]{};:,.?/";
        String all = upper + lower + digits + symbols;
        java.util.Random r = new java.util.Random();
        char[] out = new char[8];
        out[0] = upper.charAt(r.nextInt(upper.length()));
        out[1] = lower.charAt(r.nextInt(lower.length()));
        out[2] = digits.charAt(r.nextInt(digits.length()));
        out[3] = symbols.charAt(r.nextInt(symbols.length()));
        for (int i=4;i<8;i++) out[i] = all.charAt(r.nextInt(all.length()));
        // shuffle
        for (int i=7;i>0;i--){ int j = r.nextInt(i+1); char t=out[i]; out[i]=out[j]; out[j]=t; }
        String pwd = new String(out);
        txtPass.setText(pwd);
        if (strengthBar != null) strengthBar.setProgress(strength(pwd));
        items.add(new PasswordEntry(txtSite.getText(), txtUser.getText(), pwd, txtNote.getText()));
    }
}
