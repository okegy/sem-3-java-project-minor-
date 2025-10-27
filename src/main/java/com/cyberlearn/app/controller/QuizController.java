package com.cyberlearn.app.controller;

import com.cyberlearn.app.model.QuizQuestion;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizController {
    @FXML private Label lblPrompt;
    @FXML private ToggleGroup optionsGroup;
    @FXML private RadioButton optA;
    @FXML private RadioButton optB;
    @FXML private RadioButton optC;
    @FXML private Label lblScore;

    private List<QuizQuestion> bank;
    private int i = 0;
    private int score = 0;
    private static String currentWeek = null;

    public static void setCurrentWeek(String week) {
        currentWeek = week;
    }

    @FXML
    public void initialize() {
        bank = sampleBank();
        // Filter by week if specified
        if (currentWeek != null) {
            bank = filterByWeek(bank, currentWeek);
            lblPrompt.setText("Loading " + currentWeek + " quiz...");
        }
        if (!bank.isEmpty()) {
            load(i);
        } else {
            lblPrompt.setText("No questions available for this week yet.");
        }
    }
    
    private List<QuizQuestion> filterByWeek(List<QuizQuestion> all, String weekId) {
        List<QuizQuestion> filtered = new ArrayList<>();
        String weekPrefix = weekId + ":";
        for (QuizQuestion q : all) {
            if (q.getPrompt().startsWith(weekPrefix)) {
                filtered.add(q);
            }
        }
        return filtered;
    }

    private void load(int idx){
        QuizQuestion q = bank.get(idx);
        lblPrompt.setText(q.getPrompt());
        optA.setText(q.getOptions().get(0));
        optB.setText(q.getOptions().get(1));
        optC.setText(q.getOptions().get(2));
        optionsGroup.selectToggle(null);
        lblScore.setText("Score: " + score + " / " + bank.size());
    }

    @FXML
    public void onNext(){
        Toggle t = optionsGroup.getSelectedToggle();
        if (t == null) return;
        int picked = t == optA ? 0 : t == optB ? 1 : 2;
        if (picked == bank.get(i).getCorrectIndex()) score++; 
        try{var p=com.cyberlearn.app.util.ProgressService.get(com.cyberlearn.app.util.AuthService.getCurrent().getUsername()); p.setQuizAttempts(p.getQuizAttempts()+1); p.setQuizScore(score); com.cyberlearn.app.util.ProgressService.update(p);}catch(Exception ex){}
        i = (i + 1) % bank.size();
        load(i);
    }

    private List<QuizQuestion> sampleBank(){
        List<QuizQuestion> list = new ArrayList<>();
        
        // Week 1: Cyber Hygiene & Passwords
        list.add(new QuizQuestion("W1: What is the minimum recommended password length?", Arrays.asList("8 characters","12+ characters","6 characters"), 1));
        list.add(new QuizQuestion("W1: What does MFA stand for?", Arrays.asList("Multi-Factor Authentication","Main File Access","My First Account"), 0));
        list.add(new QuizQuestion("W1: Should you reuse passwords across sites?", Arrays.asList("Yes, easier to remember","No, each site needs unique password","Only for unimportant sites"), 1));
        list.add(new QuizQuestion("W1: Which is a good password manager?", Arrays.asList("Notepad on desktop","Bitwarden/LastPass","Browser autosave only"), 1));
        list.add(new QuizQuestion("W1: How often should you update software?", Arrays.asList("Never","Within 48 hours of release","Once a year"), 1));
        list.add(new QuizQuestion("W1: What makes a strong password?", Arrays.asList("Your birthdate","Mix of upper/lower/numbers/symbols","Common words"), 1));
        list.add(new QuizQuestion("W1: What is 2FA?", Arrays.asList("Two-Factor Authentication","Two File Attachments","Twice Failed Attempts"), 0));
        list.add(new QuizQuestion("W1: Should you enable MFA on email?", Arrays.asList("Yes, always","No, too inconvenient","Only for banking"), 0));
        list.add(new QuizQuestion("W1: What's the 3-2-1 backup rule?", Arrays.asList("3 copies, 2 media types, 1 offsite","3 passwords, 2 accounts, 1 manager","3 hours, 2 days, 1 week"), 0));
        list.add(new QuizQuestion("W1: Should you share passwords via email?", Arrays.asList("Yes, if encrypted","No, never","Only with IT department"), 1));
        
        // Week 2: Phishing & Social Engineering
        list.add(new QuizQuestion("W2: What is phishing?", Arrays.asList("Fishing website","Fraudulent attempt to steal information","Video game"), 1));
        list.add(new QuizQuestion("W2: What should you do before clicking a link?", Arrays.asList("Click immediately","Hover to check the URL","Forward to friends"), 1));
        list.add(new QuizQuestion("W2: What is vishing?", Arrays.asList("Virus fishing","Voice phishing via phone","Visual hacking"), 1));
        list.add(new QuizQuestion("W2: Which is a phishing red flag?", Arrays.asList("Urgent language and threats","Professional formatting","Correct grammar"), 0));
        list.add(new QuizQuestion("W2: What is spear phishing?", Arrays.asList("Fishing with spears","Targeted phishing attack","Mass email campaign"), 1));
        list.add(new QuizQuestion("W2: How to verify a suspicious email?", Arrays.asList("Click all links","Contact sender through separate channel","Reply asking if real"), 1));
        list.add(new QuizQuestion("W2: What is pretexting?", Arrays.asList("Writing before texting","Creating false scenario to steal info","Text message preview"), 1));
        list.add(new QuizQuestion("W2: Should you trust urgent payment requests via email?", Arrays.asList("Yes, always","No, verify first","Only from boss"), 1));
        list.add(new QuizQuestion("W2: What is smishing?", Arrays.asList("Small phishing","SMS/text message phishing","Smiling while fishing"), 1));
        list.add(new QuizQuestion("W2: How to spot fake sender emails?", Arrays.asList("Check domain carefully","Trust display name","Look at subject only"), 0));
        
        // Week 3: Malware 101
        list.add(new QuizQuestion("W3: What is ransomware?", Arrays.asList("Free software","Malware that encrypts files for ransom","Anti-virus software"), 1));
        list.add(new QuizQuestion("W3: What's the difference between virus and worm?", Arrays.asList("No difference","Worm spreads without user action","Virus is smaller"), 1));
        list.add(new QuizQuestion("W3: What is a Trojan?", Arrays.asList("Antivirus program","Malware disguised as legitimate software","Greek warrior"), 1));
        list.add(new QuizQuestion("W3: Should you scan USB drives before use?", Arrays.asList("Yes, always","No, unnecessary","Only if from stranger"), 0));
        list.add(new QuizQuestion("W3: What is spyware?", Arrays.asList("Spy movie software","Malware that steals information","Screen recording tool"), 1));
        list.add(new QuizQuestion("W3: How often should you update antivirus?", Arrays.asList("Never","Automatically and regularly","Once a month"), 1));
        list.add(new QuizQuestion("W3: What is a rootkit?", Arrays.asList("Plant roots","Malware that hides its presence","Admin toolkit"), 1));
        list.add(new QuizQuestion("W3: Should you download pirated software?", Arrays.asList("Yes, it's free","No, high malware risk","Only from trusted pirates"), 1));
        list.add(new QuizQuestion("W3: What's the 3-2-1 backup rule for ransomware defense?", Arrays.asList("3 copies, 2 media, 1 offsite","3 passwords","3 antivirus programs"), 0));
        list.add(new QuizQuestion("W3: What is adware?", Arrays.asList("Advertisement software that may be malicious","Ad blocker","Adobe software"), 0));
        
        // Week 4: Safe Browsing & HTTPS
        list.add(new QuizQuestion("W4: What does HTTPS stand for?", Arrays.asList("Hyper Text Transfer Protocol Secure","High Transfer Speed","Home Text Protocol"), 0));
        list.add(new QuizQuestion("W4: What port does HTTPS use?", Arrays.asList("80","443","8080"), 1));
        list.add(new QuizQuestion("W4: What does the padlock icon mean?", Arrays.asList("Site is safe from all attacks","Connection is encrypted","Site is government-approved"), 1));
        list.add(new QuizQuestion("W4: Should you enter passwords on HTTP sites?", Arrays.asList("Yes","No, only on HTTPS","Doesn't matter"), 1));
        list.add(new QuizQuestion("W4: What is a certificate authority?", Arrays.asList("Police department","Entity that issues digital certificates","School principal"), 1));
        list.add(new QuizQuestion("W4: What are secure cookies?", Arrays.asList("Encrypted biscuits","Cookies with Secure flag for HTTPS only","Cookie recipes"), 1));
        list.add(new QuizQuestion("W4: Does incognito mode hide activity from ISP?", Arrays.asList("Yes, completely","No, only from local browser history","Only on weekends"), 1));
        list.add(new QuizQuestion("W4: Should you enable third-party cookies?", Arrays.asList("Yes, always","No, disable for privacy","Only for shopping"), 1));
        list.add(new QuizQuestion("W4: What does TLS stand for?", Arrays.asList("Transport Layer Security","Top Level Security","Time Limited Session"), 0));
        list.add(new QuizQuestion("W4: Which browser extension improves security?", Arrays.asList("HTTPS Everywhere","More toolbars","Auto-clicker"), 0));
        
        // Week 5: OSI & TCP/IP
        list.add(new QuizQuestion("W5: How many layers in OSI model?", Arrays.asList("5","7","3"), 1));
        list.add(new QuizQuestion("W5: Which layer handles IP addresses?", Arrays.asList("Physical","Network (Layer 3)","Application"), 1));
        list.add(new QuizQuestion("W5: What's the difference between TCP and UDP?", Arrays.asList("No difference","TCP is reliable, UDP is fast","TCP is faster"), 1));
        list.add(new QuizQuestion("W5: What is encapsulation?", Arrays.asList("Wrapping data in headers at each layer","Encrypting data","Compressing files"), 0));
        list.add(new QuizQuestion("W5: Which layer do switches operate on?", Arrays.asList("Physical","Data Link (Layer 2)","Network"), 1));
        list.add(new QuizQuestion("W5: What is the TCP three-way handshake?", Arrays.asList("SYN, SYN-ACK, ACK","Three connections","Three passwords"), 0));
        list.add(new QuizQuestion("W5: Which protocol is connectionless?", Arrays.asList("TCP","UDP","HTTP"), 1));
        list.add(new QuizQuestion("W5: What layer do routers operate on?", Arrays.asList("Data Link","Network (Layer 3)","Transport"), 1));
        list.add(new QuizQuestion("W5: How many layers in TCP/IP model?", Arrays.asList("7","4","5"), 1));
        list.add(new QuizQuestion("W5: Which is a Layer 7 protocol?", Arrays.asList("IP","TCP","HTTP"), 2));
        
        // Week 6: Ports & Services
        list.add(new QuizQuestion("W6: Which port is used for HTTP?", Arrays.asList("443","80","22"), 1));
        list.add(new QuizQuestion("W6: Which port is used for HTTPS?", Arrays.asList("80","443","8080"), 1));
        list.add(new QuizQuestion("W6: Which port is used for SSH?", Arrays.asList("21","22","23"), 1));
        list.add(new QuizQuestion("W6: What is the port range?", Arrays.asList("0-65535","0-1023","1-100"), 0));
        list.add(new QuizQuestion("W6: Which port is used for FTP?", Arrays.asList("20/21","80","443"), 0));
        list.add(new QuizQuestion("W6: Should Telnet (port 23) be used?", Arrays.asList("Yes, always","No, it's insecure","Only internally"), 1));
        list.add(new QuizQuestion("W6: Which port is used for DNS?", Arrays.asList("25","53","80"), 1));
        list.add(new QuizQuestion("W6: Which port is used for SMTP?", Arrays.asList("25","80","110"), 0));
        list.add(new QuizQuestion("W6: Which port is used for MySQL?", Arrays.asList("3306","3389","1433"), 0));
        list.add(new QuizQuestion("W6: What are well-known ports?", Arrays.asList("0-1023","Famous websites","1024-49151"), 0));
        
        // Week 7: Firewalls & NAT
        list.add(new QuizQuestion("W7: What is a firewall?", Arrays.asList("Physical wall","Network security system","Fire extinguisher"), 1));
        list.add(new QuizQuestion("W7: What's the difference between stateful and stateless firewalls?", Arrays.asList("No difference","Stateful tracks connections","Stateless is better"), 1));
        list.add(new QuizQuestion("W7: What does NAT stand for?", Arrays.asList("Network Address Translation","New Authentication Token","National Access Terminal"), 0));
        list.add(new QuizQuestion("W7: What is the main benefit of NAT?", Arrays.asList("Faster internet","IP address conservation","Better graphics"), 1));
        list.add(new QuizQuestion("W7: What is a DMZ in networking?", Arrays.asList("Demilitarized Zone between networks","Dangerous Malware Zone","Data Management Zone"), 0));
        list.add(new QuizQuestion("W7: What does a packet filter firewall check?", Arrays.asList("File size","IP addresses and ports","Email content"), 1));
        list.add(new QuizQuestion("W7: What is WAF?", Arrays.asList("Web Application Firewall","Wireless Access Filter","Windows Authentication Framework"), 0));
        list.add(new QuizQuestion("W7: Does NAT provide security?", Arrays.asList("Yes, perfect security","No security at all","Basic security layer by hiding internal IPs"), 2));
        list.add(new QuizQuestion("W7: What is PAT?", Arrays.asList("Port Address Translation","Public Access Token","Password Authentication Tool"), 0));
        list.add(new QuizQuestion("W7: What's a next-gen firewall feature?", Arrays.asList("Deep packet inspection","Faster speed only","Cheaper cost"), 0));
        
        // Week 8: Wireshark
        list.add(new QuizQuestion("W8: What is Wireshark?", Arrays.asList("Shark simulator","Network protocol analyzer","Antivirus"), 1));
        list.add(new QuizQuestion("W8: What does Wireshark capture?", Arrays.asList("Screenshots","Network packets","Viruses"), 1));
        list.add(new QuizQuestion("W8: Which filter shows HTTP traffic?", Arrays.asList("http","tcp.port==80","web"), 0));
        list.add(new QuizQuestion("W8: What does 'Follow TCP Stream' do?", Arrays.asList("Watch rivers","Reconstruct full conversation","Delete packets"), 1));
        list.add(new QuizQuestion("W8: Which filter shows specific IP?", Arrays.asList("ip==192.168.1.1","ip.addr == 192.168.1.1","show ip 192.168.1.1"), 1));
        list.add(new QuizQuestion("W8: Can Wireshark decrypt HTTPS?", Arrays.asList("Yes, always","No, without keys","Only on Tuesdays"), 1));
        list.add(new QuizQuestion("W8: What is a display filter?", Arrays.asList("Monitor setting","Filter applied after capture","Capture prevention"), 1));
        list.add(new QuizQuestion("W8: Which protocol can Wireshark NOT analyze?", Arrays.asList("HTTP","DNS","Encrypted content without keys"), 2));
        list.add(new QuizQuestion("W8: What's the file extension for Wireshark captures?", Arrays.asList(".pcap or .pcapng",".txt",".exe"), 0));
        list.add(new QuizQuestion("W8: Should you analyze others' traffic without permission?", Arrays.asList("Yes, for learning","No, illegal and unethical","Only at work"), 1));
        
        return list;
    }
}
