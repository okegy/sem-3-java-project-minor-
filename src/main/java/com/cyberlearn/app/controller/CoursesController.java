package com.cyberlearn.app.controller;

import com.cyberlearn.app.model.Material;
import com.cyberlearn.app.util.MaterialsService;
import javafx.scene.web.WebView;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class CoursesController {
    @FXML private WebView web;
    private java.util.List<Material> materials;
    @FXML private ListView<String> list;
    @FXML private TextArea notes;

    @FXML
    public void initialize() {
        try{
            materials = MaterialsService.all();
            for (var m : materials) list.getItems().add(m.getLevel()+": "+m.getTitle());
            list.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
                int idx = list.getSelectionModel().getSelectedIndex();
                if (idx>=0){ 
                    var m = materials.get(idx);
                    String links = (m.getLinks()==null||m.getLinks().isEmpty())?"":("\nLinks:\n"+String.join("\n", m.getLinks()));
                    notes.setText(m.getDescription()+links);
                    try{
                        if (m.getLinks()!=null && !m.getLinks().isEmpty()){
                            String url = m.getLinks().get(0);
                            if (url.contains("youtube.com") || url.contains("youtu.be")){
                                // Create clickable link to open in browser
                                String html = "<html><body style='background: #1a1a2e; color: #eee; font-family: Arial; padding: 20px;'>" +
                                              "<h2>Video Lesson</h2>" +
                                              "<p>Click the button below to watch this lesson on YouTube:</p>" +
                                              "<button onclick=\"window.location.href='" + url + "'\" style='display: inline-block; background: #00ff88; color: black; padding: 12px 24px; border: none; font-weight: bold; border-radius: 4px; margin-top: 12px; cursor: pointer;'>" +
                                              "Open in YouTube</button>" +
                                              "<p style='margin-top: 20px; font-size: 12px; color: #888;'>Tip: The video will open in your default browser</p>" +
                                              "</body></html>";
                                web.getEngine().loadContent(html);
                                // Add click listener to WebView for link navigation
                                web.getEngine().setOnStatusChanged(e -> {
                                    if (e.getData() != null && e.getData().startsWith("http")) {
                                        try {
                                            java.awt.Desktop.getDesktop().browse(new java.net.URI(e.getData()));
                                        } catch(Exception ignored) {}
                                    }
                                });
                            } else {
                                web.getEngine().loadContent("<p>No YouTube link for this lesson. Links listed below.</p>");
                            }
                        }
                    } catch(Exception ex){ /* ignore */ }
                }
            });
            if(!materials.isEmpty()) list.getSelectionModel().select(0);
        }catch(Exception e){ 
            notes.setText("Error loading materials: "+e.getMessage()); 
        }
    }

    @FXML
    public void onTakeQuiz(){
        try{
            int idx = list.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && materials != null && materials.size() > idx) {
                String lessonId = materials.get(idx).getId();
                // Store the selected week for quiz filtering
                QuizController.setCurrentWeek(lessonId);
                // Navigate to Quiz tab
                javafx.scene.control.TabPane tabPane = (javafx.scene.control.TabPane) list.getScene().lookup(".tab-pane");
                if (tabPane != null) {
                    // Find Quiz tab (should be last tab)
                    tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
                    notes.appendText("\n\nNavigating to " + lessonId + " quiz...");
                }
            }
        }catch(Exception e){
            notes.appendText("\nError navigating to quiz: " + e.getMessage());
        }
    }

    @FXML
    public void onComplete(){
        try{
            var u = com.cyberlearn.app.util.AuthService.getCurrent();
            var p = com.cyberlearn.app.util.ProgressService.get(u.getUsername());
            int idx = list.getSelectionModel().getSelectedIndex();
            
            if (idx >= 0 && materials != null && materials.size() > idx) {
                String lessonId = materials.get(idx).getId();
                String title = materials.get(idx).getTitle();
                
                // Track completed lesson
                if (!p.getCompletedLessons().contains(lessonId)) {
                    p.getCompletedLessons().add(lessonId);
                    p.setQuizScore(p.getQuizScore() + 5); // Award 5 XP per lesson
                    com.cyberlearn.app.util.ProgressService.update(p);
                    
                    // Generate certificate
                    var path = com.cyberlearn.app.util.CertificateService.makeCertificate(u.getUsername(), title);
                    notes.appendText("\n\n*** LESSON COMPLETED! ***\n");
                    notes.appendText("+ 5 XP awarded!\n");
                    notes.appendText("Progress: " + p.getCompletedLessons().size() + "/52 lessons completed\n");
                    notes.appendText("Certificate: " + path.toAbsolutePath());
                } else {
                    notes.appendText("\n\nYou already completed this lesson!");
                }
            }
        }catch(Exception e){
            notes.appendText("\nError: " + e.getMessage());
        }
    }
}
