package com.cyberlearn.app.controller;

import com.cyberlearn.app.util.PortScanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PortScannerController {
    @FXML private TextField txtHost;
    @FXML private TextField txtStart;
    @FXML private TextField txtEnd;
    @FXML private TableView<PortRow> table;
    @FXML private TableColumn<PortRow, Number> colPort;
    @FXML private TableColumn<PortRow, String> colStatus;
    @FXML private Label lblStatus;

    public static class PortRow {
        public final int port;
        public final String status;
        public PortRow(int p, String s){ port = p; status = s; }
        public int getPort(){ return port; }
        public String getStatus(){ return status; }
    }

    @FXML
    public void initialize() {
        colPort.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPort()));
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
    }

    @FXML
    public void onScan() {
        String host = txtHost.getText().isBlank() ? "127.0.0.1" : txtHost.getText().trim();
        int s = Integer.parseInt(txtStart.getText());
        int e = Integer.parseInt(txtEnd.getText());
        lblStatus.setText("Scanning...");
        new Thread(() -> {
            try {
                var results = PortScanner.scan(host, s, e, 200, 100);
                ObservableList<PortRow> rows = FXCollections.observableArrayList();
                for (var r : results) {
                    if (r.port < 0) continue;
                    rows.add(new PortRow(r.port, r.open ? "OPEN" : "CLOSED"));
                }
                javafx.application.Platform.runLater(() -> {
                    table.setItems(rows);
                    lblStatus.setText("Done ✔");
                    try{var p=com.cyberlearn.app.util.ProgressService.get(com.cyberlearn.app.util.AuthService.getCurrent().getUsername()); p.setScanUses(p.getScanUses()+1); com.cyberlearn.app.util.ProgressService.update(p);}catch(Exception ex){}
                });
            } catch (Exception ex) {
                javafx.application.Platform.runLater(() -> lblStatus.setText("Error: " + ex.getMessage()));
            }
        }).start();
    }
}
