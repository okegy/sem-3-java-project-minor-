package com.cyberlearn.app.controller;

import com.cyberlearn.app.util.AuthService;
import com.cyberlearn.app.util.ProgressService;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class StageMapController {
    @FXML private HBox worlds;
    @FXML private Label lblStatus;

    @FXML
    public void initialize(){
        try {
            var p = ProgressService.get(AuthService.getCurrent().getUsername());
            int xp = p.getEncUses()*10 + p.getScanUses()*10 + p.getHashUses()*10 + p.getPassMgrUses()*10 + p.getQuizScore()*5;
            double pct = Math.min(1.0, xp/200.0);
            int unlocked = (int)Math.ceil(pct*5); // 5 worlds
            if (unlocked<1) unlocked=1;
            for (int i=0;i<5;i++){
                ImageView iv = (ImageView) worlds.getChildren().get(i);
                iv.setOpacity(i<unlocked?1.0:0.25);
                FadeTransition ft = new FadeTransition(Duration.millis(600), iv);
                ft.setFromValue(0.25);
                ft.setToValue(i<unlocked?1.0:0.25);
                ft.play();
            }
            lblStatus.setText("Worlds unlocked: " + unlocked + "/5");
        } catch (Exception e) {
            lblStatus.setText("Worlds unlocked: 1/5");
        }
    }
}
