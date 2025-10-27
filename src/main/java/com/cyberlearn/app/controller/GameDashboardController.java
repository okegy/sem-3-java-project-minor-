package com.cyberlearn.app.controller;

import com.cyberlearn.app.util.AuthService;
import com.cyberlearn.app.util.ProgressService;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class GameDashboardController {
    @FXML private AnchorPane track;
    @FXML private ImageView hero;
    @FXML private Label lblPct;

    @FXML
    public void initialize(){
        try {
            var p = ProgressService.get(AuthService.getCurrent().getUsername());
            int xp = p.getEncUses()*10 + p.getScanUses()*10 + p.getHashUses()*10 + p.getPassMgrUses()*10 + p.getQuizScore()*5;
            double pct = Math.min(1.0, xp/200.0);
            lblPct.setText("Completion: " + (int)(pct*100) + "%");
            track.widthProperty().addListener((o,ov,nv)->moveHero(pct));
            moveHero(pct);
        } catch (Exception e) {
            lblPct.setText("Completion: 0%");
        }
    }

    private void moveHero(double pct){
        double width = track.getWidth();
        double x = (width - 64) * pct;
        TranslateTransition t = new TranslateTransition(Duration.millis(700), hero);
        t.setToX(x);
        t.play();
    }
}
