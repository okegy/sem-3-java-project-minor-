package com.cyberlearn.app.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CertificateService {
    public static Path makeCertificate(String username, String courseTitle) throws Exception {
        Path dir = Path.of("reports");
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Path pdf = dir.resolve(username + "_certificate.pdf");
        
        // Create document with custom page size
        Document doc = new Document(PageSize.A4.rotate()); // Landscape for certificate
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf.toFile()));
        doc.open();
        
        // Add decorative border
        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorStroke(new Color(0, 102, 204)); // Blue border
        canvas.setLineWidth(3f);
        canvas.rectangle(30, 30, PageSize.A4.rotate().getWidth() - 60, PageSize.A4.rotate().getHeight() - 60);
        canvas.stroke();
        
        // Inner border
        canvas.setColorStroke(new Color(0, 153, 255)); // Lighter blue
        canvas.setLineWidth(1f);
        canvas.rectangle(40, 40, PageSize.A4.rotate().getWidth() - 80, PageSize.A4.rotate().getHeight() - 80);
        canvas.stroke();
        
        // Add spacing from top
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        
        // Title with custom font and color
        Font titleFont = new Font(Font.TIMES_ROMAN, 42, Font.BOLD);
        titleFont.setColor(new Color(0, 102, 204));
        Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        
        // Subtitle
        doc.add(new Paragraph(" "));
        Font subtitleFont = new Font(Font.HELVETICA, 16, Font.ITALIC);
        subtitleFont.setColor(new Color(64, 64, 64));
        Paragraph subtitle = new Paragraph("CyberLearn Security Training Program", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        doc.add(subtitle);
        
        // Spacing
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        
        // This is to certify
        Font normalFont = new Font(Font.HELVETICA, 14);
        Paragraph certifyText = new Paragraph("This is to certify that", normalFont);
        certifyText.setAlignment(Element.ALIGN_CENTER);
        doc.add(certifyText);
        
        doc.add(new Paragraph(" "));
        
        // Student name with underline effect
        Font nameFont = new Font(Font.TIMES_ROMAN, 28, Font.BOLD);
        nameFont.setColor(new Color(0, 0, 0));
        Paragraph namePara = new Paragraph(username.toUpperCase(), nameFont);
        namePara.setAlignment(Element.ALIGN_CENTER);
        doc.add(namePara);
        
        // Underline for name
        Paragraph underline = new Paragraph("_".repeat(40), new Font(Font.HELVETICA, 12));
        underline.setAlignment(Element.ALIGN_CENTER);
        underline.setSpacingBefore(-10);
        doc.add(underline);
        
        doc.add(new Paragraph(" "));
        
        // Course completion text
        Font courseFont = new Font(Font.HELVETICA, 14);
        Paragraph coursePara = new Paragraph("has successfully completed the course", courseFont);
        coursePara.setAlignment(Element.ALIGN_CENTER);
        doc.add(coursePara);
        
        doc.add(new Paragraph(" "));
        
        // Course title
        Font courseTitleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        courseTitleFont.setColor(new Color(0, 102, 204));
        Paragraph courseTitle2 = new Paragraph(courseTitle, courseTitleFont);
        courseTitle2.setAlignment(Element.ALIGN_CENTER);
        doc.add(courseTitle2);
        
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        
        // Date and Time with formatting
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        
        Font dateFont = new Font(Font.HELVETICA, 12);
        
        // Create table for date/time and signatures
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(85);
        table.setSpacingBefore(20);
        
        // Date issued cell
        PdfPCell dateCell = new PdfPCell();
        dateCell.setBorder(Rectangle.NO_BORDER);
        dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph dateText = new Paragraph();
        dateText.add(new Chunk("Date Issued:\n", new Font(Font.HELVETICA, 10, Font.BOLD)));
        dateText.add(new Chunk(now.format(dateFormatter) + "\n", dateFont));
        dateText.add(new Chunk("Time: " + now.format(timeFormatter), new Font(Font.HELVETICA, 9)));
        dateCell.addElement(dateText);
        table.addCell(dateCell);
        
        // Certificate ID cell
        PdfPCell idCell = new PdfPCell();
        idCell.setBorder(Rectangle.NO_BORDER);
        idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph idText = new Paragraph();
        idText.add(new Chunk("Certificate ID:\n", new Font(Font.HELVETICA, 10, Font.BOLD)));
        String certId = "CL-" + username.toUpperCase().substring(0, Math.min(3, username.length())) + 
                       "-" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"));
        idText.add(new Chunk(certId, new Font(Font.COURIER, 10, Font.BOLD)));
        idCell.addElement(idText);
        table.addCell(idCell);
        
        doc.add(table);
        
        // Signature section
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        
        PdfPTable sigTable = new PdfPTable(2);
        sigTable.setWidthPercentage(80);
        
        // Instructor signature
        PdfPCell sig1 = new PdfPCell();
        sig1.setBorder(Rectangle.NO_BORDER);
        sig1.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph sig1Text = new Paragraph();
        sig1Text.add(new Chunk("_".repeat(25) + "\n", new Font(Font.HELVETICA, 10)));
        sig1Text.add(new Chunk("Instructor Signature\n", new Font(Font.HELVETICA, 9)));
        sig1Text.add(new Chunk("CyberLearn Academy", new Font(Font.HELVETICA, 8, Font.ITALIC)));
        sig1.addElement(sig1Text);
        sigTable.addCell(sig1);
        
        // Director signature
        PdfPCell sig2 = new PdfPCell();
        sig2.setBorder(Rectangle.NO_BORDER);
        sig2.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph sig2Text = new Paragraph();
        sig2Text.add(new Chunk("_".repeat(25) + "\n", new Font(Font.HELVETICA, 10)));
        sig2Text.add(new Chunk("Director Signature\n", new Font(Font.HELVETICA, 9)));
        sig2Text.add(new Chunk("CyberLearn Academy", new Font(Font.HELVETICA, 8, Font.ITALIC)));
        sig2.addElement(sig2Text);
        sigTable.addCell(sig2);
        
        doc.add(sigTable);
        
        // Footer
        doc.add(new Paragraph(" "));
        Font footerFont = new Font(Font.HELVETICA, 8, Font.ITALIC);
        footerFont.setColor(new Color(128, 128, 128));
        Paragraph footer = new Paragraph("This certificate is digitally generated and certifies successful completion of the cybersecurity training module.", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);
        
        doc.close();
        return pdf;
    }
}
