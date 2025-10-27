package com.cyberlearn.app.controller;

import com.cyberlearn.app.util.HashUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public class HashController {
    @FXML private TextField txtAlgorithm;
    @FXML private TextArea txtOutput;
    @FXML private Label lblStatus;
    private File currentFile;

    @FXML
    public void onPickFile() {
        FileChooser fc = new FileChooser();
        currentFile = fc.showOpenDialog(null);
        if (currentFile != null) {
            lblStatus.setText("Selected: " + currentFile.getName());
        }
    }

    @FXML
    public void onHash() {
        try {
            String alg = txtAlgorithm.getText().isBlank() ? "SHA-256" : txtAlgorithm.getText().trim();
            String h = HashUtil.hash(currentFile.toPath(), alg);
            txtOutput.setText(h);
            lblStatus.setText("Hashed ✔");
            try{var p=com.cyberlearn.app.util.ProgressService.get(com.cyberlearn.app.util.AuthService.getCurrent().getUsername()); p.setHashUses(p.getHashUses()+1); com.cyberlearn.app.util.ProgressService.update(p);}catch(Exception ex){}
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }
}
