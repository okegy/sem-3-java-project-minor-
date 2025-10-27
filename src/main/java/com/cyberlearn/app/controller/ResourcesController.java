package com.cyberlearn.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ResourcesController {
    @FXML private ListView<String> list;
    @FXML private TextArea details;

    @FXML
    public void initialize(){
        list.getItems().addAll(
                "Wireshark — https://www.wireshark.org/",
                "Nmap — https://nmap.org/",
                "OWASP ZAP — https://www.zaproxy.org/",
                "Burp Suite Community — https://portswigger.net/burp",
                "Metasploit — https://www.metasploit.com/",
                "Kali Linux — https://www.kali.org/",
                "Docker — https://www.docker.com/",
                "Git — https://git-scm.com/"
        );
        list.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->{
            if (nv!=null) details.setText("Open in your browser:\n" + nv);
        });
    }
}
