package com.cyberlearn.app.util;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CertificateGenerator {
    
    public static void showCertificate(String studentName) {
        Stage certificateStage = new Stage();
        certificateStage.setTitle("Certificate of Completion");
        
        // Create main container
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 40;");
        
        // Create certificate container with border
        VBox certificate = new VBox(20);
        certificate.setAlignment(Pos.CENTER);
        certificate.setStyle(
            "-fx-background-color: white; " +
            "-fx-padding: 50; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        certificate.setMaxWidth(800);
        
        // Add decorative header
        Label header = new Label("CERTIFICATE OF COMPLETION");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Add decorative line
        StackPane line = new StackPane();
        line.setStyle("-fx-background-color: #3498db; -fx-min-height: 3px; -fx-max-width: 200px;");
        
        // Add certificate content
        Label presentText = new Label("This is to certify that");
        presentText.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        
        // Student name
        Label nameLabel = new Label(studentName);
        nameLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-padding: 10 0;");
        
        // Completion text
        Label completionText = new Label(
            "has successfully completed the 52-Week Cybersecurity Mastery Program\n" +
            "demonstrating proficiency in various cybersecurity domains and techniques."
        );
        completionText.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e; -fx-text-alignment: center;");
        completionText.setTextAlignment(TextAlignment.CENTER);
        completionText.setWrapText(true);
        completionText.setMaxWidth(600);
        
        // Date
        Label dateLabel = new Label("Completed on: " + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        // Signature line
        VBox signatureBox = new VBox(5);
        signatureBox.setAlignment(Pos.CENTER);
        
        StackPane signatureLine = new StackPane();
        signatureLine.setStyle("-fx-background-color: #bdc3c7; -fx-min-height: 1px; -fx-max-width: 200px;");
        
        Label signatureLabel = new Label("CyberLearn Academy");
        signatureLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3c4c;");
        
        Label signatureTitle = new Label("Cybersecurity Education");
        signatureTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        signatureBox.getChildren().addAll(signatureLine, signatureLabel, signatureTitle);
        
        // Add all elements to certificate
        certificate.getChildren().addAll(
            header,
            line,
            presentText,
            nameLabel,
            completionText,
            dateLabel,
            signatureBox
        );
        
        // Add certificate to main container
        mainContainer.getChildren().add(certificate);
        
        // Create scene and show stage
        Scene scene = new Scene(mainContainer, 900, 700);
        certificateStage.setScene(scene);
        
        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), mainContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        // Center the window on screen
        certificateStage.centerOnScreen();
        
        // Show the stage after the animation
        certificateStage.show();
        fadeIn.play();
    }
}