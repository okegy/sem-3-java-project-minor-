package com.cyberlearn.app.controller;

import com.cyberlearn.app.Navigator;
import com.cyberlearn.app.model.Material;
import com.cyberlearn.app.util.MaterialsService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminMaterialsController {
    @FXML private TableView<Material> table;
    @FXML private TableColumn<Material, String> colTitle;
    @FXML private TableColumn<Material, String> colLevel;
    @FXML private TextField txtTitle;
    @FXML private TextField txtLevel;
    @FXML private TextArea txtDesc;
    @FXML private TextArea txtLinks;
    @FXML private Label lblStatus;

    private ObservableList<Material> items = FXCollections.observableArrayList();
    private Material editing = null;

    @FXML
    public void initialize(){
        colTitle.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));
        colLevel.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLevel()));
        try {
            items.setAll(MaterialsService.all());
            table.setItems(items);
        } catch (Exception e) { lblStatus.setText("Error: " + e.getMessage()); }
    }

    @FXML
    private void onBack() {
        Navigator.go("admin.fxml");

        table.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->{
            if (nv==null) return;
            editing = nv;
            txtTitle.setText(nv.getTitle());
            txtLevel.setText(nv.getLevel());
            txtDesc.setText(nv.getDescription());
            txtLinks.setText(String.join("\n", nv.getLinks()==null? java.util.List.of() : nv.getLinks()));
        });
    }

    @FXML
    public void onNew(){
        editing = null;
        txtTitle.clear(); txtLevel.clear(); txtDesc.clear(); txtLinks.clear();
    }

    @FXML
    public void onSave(){
        try {
            java.util.List<String> links = java.util.Arrays.asList(txtLinks.getText().split("\\R"));
            if (editing == null){
                Material m = new Material(null, txtTitle.getText(), txtLevel.getText(), txtDesc.getText(), links);
                MaterialsService.add(m);
                items.setAll(MaterialsService.all());
                lblStatus.setText("Added ✔");
            } else {
                editing.setTitle(txtTitle.getText());
                editing.setLevel(txtLevel.getText());
                editing.setDescription(txtDesc.getText());
                editing.setLinks(links);
                MaterialsService.update(editing);
                items.setAll(MaterialsService.all());
                lblStatus.setText("Updated ✔");
            }
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void onDelete(){
        try{
            Material m = table.getSelectionModel().getSelectedItem();
            if (m==null){ lblStatus.setText("Select a row"); return; }
            MaterialsService.delete(m.getId());
            items.setAll(MaterialsService.all());
            lblStatus.setText("Deleted ✔");
        }catch(Exception e){ lblStatus.setText("Error: " + e.getMessage()); }
    }

    @FXML
    public void onExportBooklet(){
        try{
            var list = com.cyberlearn.app.util.MaterialsService.all();
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setInitialFileName("CyberLearn_Curriculum_Booklet.pdf");
            java.io.File f = fc.showSaveDialog(null);
            if (f==null) return;
            com.lowagie.text.Document doc = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(f));
            doc.open();
            com.lowagie.text.Font title = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD);
            com.lowagie.text.Font text = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12);
            doc.add(new com.lowagie.text.Paragraph("CyberLearn — 52 Week Curriculum", title));
            for (var m : list){
                doc.add(new com.lowagie.text.Paragraph(m.getLevel()+": "+m.getTitle(), title));
                doc.add(new com.lowagie.text.Paragraph(m.getDescription(), text));
                if (m.getLinks()!=null){
                    for (String link : m.getLinks()){
                        doc.add(new com.lowagie.text.Paragraph("Link: "+link, text));
                    }
                }
                doc.add(new com.lowagie.text.Paragraph(" "));
            }
            doc.close();
            lblStatus.setText("Booklet exported ✔");
        }catch(Exception e){
            lblStatus.setText("Error: "+e.getMessage());
        }
    }
}
