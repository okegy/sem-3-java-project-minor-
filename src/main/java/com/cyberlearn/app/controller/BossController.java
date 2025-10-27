package com.cyberlearn.app.controller;

import com.cyberlearn.app.util.AuthService;
import com.cyberlearn.app.util.CertificateService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;

public class BossController {
    @FXML private Label lblQ;
    @FXML private ToggleGroup group;
    @FXML private RadioButton a;
    @FXML private RadioButton b;
    @FXML private RadioButton c;
    @FXML private ProgressBar bar;
    @FXML private Label lblStatus;

    private static class Q{ String p; String[] o; int i; Q(String p,String[] o,int i){this.p=p;this.o=o;this.i=i;}}
    private List<Q> bank;
    private int idx=0, score=0;

    @FXML
    public void initialize(){
        bank = loadBank();
        load(0);
    }

    private List<Q> loadBank(){
        List<Q> list = new ArrayList<>();
        list.add(new Q("Which port is HTTPS?", new String[]{"80","22","443"},2));
        list.add(new Q("AES is a ___ cipher", new String[]{"symmetric","asymmetric","hash"},0));
        list.add(new Q("Hashing is ___", new String[]{"reversible","one-way","encryption"},1));
        list.add(new Q("MFA stands for", new String[]{"Multi-Factor Authentication","Mail Filtering API","Memory Forensics Analysis"},0));
        list.add(new Q("MD5 vs SHA-256: which is stronger?", new String[]{"MD5","SHA-256","Same"},1));
        list.add(new Q("Firewalls are used to", new String[]{"open all ports","control network traffic","store passwords"},1));
        list.add(new Q("Public key belongs to", new String[]{"the receiver (shared)","kept secret only","the CPU"},0));
        list.add(new Q("TLS secures", new String[]{"web traffic","file compression","video framerate"},0));
        list.add(new Q("Nmap is used for", new String[]{"port scanning","photo editing","pdf reading"},0));
        list.add(new Q("SIEM aggregates", new String[]{"logs","videos","images"},0));
        return list;
    }

    private void load(int i){
        Q q = bank.get(i);
        lblQ.setText("Q"+(i+1)+": "+q.p);
        a.setText(q.o[0]); b.setText(q.o[1]); c.setText(q.o[2]);
        group.selectToggle(null);
        bar.setProgress((double)i/bank.size());
        lblStatus.setText("Score: "+score);
    }

    @FXML
    public void onNext(){
        Toggle t = group.getSelectedToggle();
        if (t==null) return;
        int pick = t==a?0:(t==b?1:2);
        if (pick==bank.get(idx).i) score++;
        idx++;
        if (idx>=bank.size()){
            bar.setProgress(1.0);
            int pass = (int)Math.ceil(bank.size()*0.7);
            if (score>=pass){
                try{
                    var user = AuthService.getCurrent();
                    com.cyberlearn.app.util.ProgressService.get(user.getUsername()); // ensure exists
                    var p = com.cyberlearn.app.util.CertificateService.makeCertificate(user.getUsername(), "CyberLearn Final Boss");
                    lblStatus.setText("PASS! Certificate: "+p.toAbsolutePath());
                }catch(Exception e){ lblStatus.setText("PASS! (certificate error: "+e.getMessage()+")"); }
            } else {
                lblStatus.setText("FAILED ("+score+"/"+bank.size()+"). Try again!");
            }
            return;
        }
        load(idx);
    }
}
