package com.cyberlearn.app;

import javafx.scene.Scene;

public class ThemeManager {
    private static boolean dark = true;
    public static void toggle(Scene scene){
        scene.getStylesheets().clear();
        if (dark){
            scene.getStylesheets().add(ThemeManager.class.getResource("/com/cyberlearn/app/css/light.css").toExternalForm());
        } else {
            scene.getStylesheets().add(ThemeManager.class.getResource("/com/cyberlearn/app/css/cyberpunk.css").toExternalForm());
        }
        dark = !dark;
    }
}
