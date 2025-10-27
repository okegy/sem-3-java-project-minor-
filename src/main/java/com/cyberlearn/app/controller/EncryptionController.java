package com.cyberlearn.app.controller;

import com.cyberlearn.app.util.CryptoUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EncryptionController {
    @FXML private TextArea txtPlain;
    @FXML private TextArea txtCipher;
    @FXML private PasswordField txtKey;
    @FXML private Label lblStatus;

    @FXML
    public void onEncrypt() {
        try {
            String out = CryptoUtil.encryptAESGCM(txtPlain.getText(), txtKey.getText());
            txtCipher.setText(out);
            lblStatus.setText("Encrypted ✔");
            try{var p=com.cyberlearn.app.util.ProgressService.get(com.cyberlearn.app.util.AuthService.getCurrent().getUsername()); p.setEncUses(p.getEncUses()+1); com.cyberlearn.app.util.ProgressService.update(p);}catch(Exception ex){}
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void onDecrypt() {
        try {
            String out = CryptoUtil.decryptAESGCM(txtCipher.getText(), txtKey.getText());
            txtPlain.setText(out);
            lblStatus.setText("Decrypted ✔");
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }
}
