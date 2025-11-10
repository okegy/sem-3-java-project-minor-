package com.cyberlearn.app.util;

import com.cyberlearn.app.model.Material;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class MaterialsService {
    private static final Path FILE = Path.of("data/materials.json");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<Material> all() throws Exception {
        if (!Files.exists(FILE)) {
            Files.createDirectories(FILE.getParent());
            List<Material> seed = seed();
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), seed);
            return seed;
        }
        return MAPPER.readValue(FILE.toFile(), new TypeReference<List<Material>>(){});
    }

    public static void saveAll(List<Material> list) throws Exception {
        Files.createDirectories(FILE.getParent());
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), list);
    }

    public static Material add(Material m) throws Exception {
        var list = all();
        if (m.getId()==null || m.getId().isBlank()) m.setId(UUID.randomUUID().toString());
        list.add(m);
        saveAll(list);
        return m;
    }

    public static void update(Material m) throws Exception {
        var list = all();
        for (int i=0;i<list.size();i++){
            if (list.get(i).getId().equals(m.getId())) {
                list.set(i, m);
                break;
            }
        }
        saveAll(list);
    }

    public static void delete(String id) throws Exception {
        var list = all();
        list.removeIf(x -> x.getId().equals(id));
        saveAll(list);
    }

    private static List<Material> seed(){
        List<Material> list = new ArrayList<>();
        
        // Quarter 1: Foundation (Weeks 1-13)
        list.add(createWeekMaterial(1, "Introduction to Cybersecurity", "Understanding the cybersecurity landscape, threats, and career paths.", "dQw4w9WgXcQ"));
        list.add(createWeekMaterial(2, "Networking Fundamentals", "OSI model, TCP/IP, network protocols, and basic network security.", "3QhU9jd03a0"));
        list.add(createWeekMaterial(3, "Operating System Security", "Windows and Linux security fundamentals, user management, and permissions.", "bPVaOlQ6ArE"));
        list.add(createWeekMaterial(4, "Cryptography Basics", "Encryption, hashing, digital signatures, and PKI.", "jhXCTbFnK8o"));
        list.add(createWeekMaterial(5, "Web Application Security", "OWASP Top 10, XSS, SQLi, and web security best practices.", "1S0aBV-Waeo"));
        list.add(createWeekMaterial(6, "Network Security", "Firewalls, IDS/IPS, VPNs, and network monitoring.", "4_zSIXb7tLQ"));
        list.add(createWeekMaterial(7, "Malware Analysis", "Types of malware, analysis techniques, and prevention.", "p1S5hCtttjU"));
        list.add(createWeekMaterial(8, "Social Engineering", "Phishing, pretexting, and other social engineering attacks.", "Vo1frFrugl0"));
        list.add(createWeekMaterial(9, "Incident Response", "Incident handling, forensic investigation, and recovery.", "1Uv95lBIGBk"));
        list.add(createWeekMaterial(10, "Cloud Security", "Cloud computing models, security challenges, and best practices.", "1SOMqcAUhW8"));
        list.add(createWeekMaterial(11, "Mobile Security", "Mobile OS security, app security, and BYOD policies.", "M8WfSnrIiOc"));
        list.add(createWeekMaterial(12, "IoT Security", "Internet of Things vulnerabilities and security measures.", "H6W8v9tTj04"));
        list.add(createWeekMaterial(13, "Review & Assessment", "Review of Q1 topics and knowledge assessment.", ""));

        // Quarter 2: Intermediate (Weeks 14-26)
        list.add(createWeekMaterial(14, "Advanced Network Defense", "SIEM, network traffic analysis, and threat hunting.", "bLV4bBh8aec"));
        list.add(createWeekMaterial(15, "Penetration Testing", "Ethical hacking methodology and tools.", "3Kq1MIfTWCE"));
        list.add(createWeekMaterial(16, "Web App Penetration Testing", "Advanced web app testing and bug hunting.", "2VSNnHUI7K8"));
        list.add(createWeekMaterial(17, "Wireless Security", "WiFi security, attacks, and defenses.", "WfYf4LtBq3I"));
        list.add(createWeekMaterial(18, "Red Team Operations", "Adversary emulation and red teaming.", "fDeLtKxZgtg"));
        list.add(createWeekMaterial(19, "Blue Team Operations", "Defensive security operations and monitoring.", "VjYFr2TZwC8"));
        list.add(createWeekMaterial(20, "Threat Intelligence", "Threat actors, intelligence lifecycle, and platforms.", "6JNGvVuwEdw"));
        list.add(createWeekMaterial(21, "Digital Forensics", "Forensic investigation techniques and tools.", "8tqHjY8mO04"));
        list.add(createWeekMaterial(22, "Malware Analysis & Reverse Engineering", "Static and dynamic analysis of malware.", "p1S5hCtttjU"));
        list.add(createWeekMaterial(23, "Secure Coding Practices", "Writing secure code and code review.", "2D9XbfbXwEQ"));
        list.add(createWeekMaterial(24, "Security Compliance & Frameworks", "NIST, ISO 27001, and compliance requirements.", "aT7JQvVk4LY"));
        list.add(createWeekMaterial(25, "Cloud Security Architecture", "Designing secure cloud environments.", "1SOMqcAUhW8"));
        list.add(createWeekMaterial(26, "Review & Assessment", "Review of Q2 topics and knowledge assessment.", ""));

        // Quarter 3: Advanced (Weeks 27-39)
        list.add(createWeekMaterial(27, "Advanced Persistent Threats", "APT groups, techniques, and mitigation.", "bLV4bBh8aec"));
        list.add(createWeekMaterial(28, "Network Traffic Analysis", "Advanced packet analysis and network forensics.", "4_zSIXb7tLQ"));
        list.add(createWeekMaterial(29, "Incident Response & Threat Hunting", "Advanced IR techniques and threat hunting.", "1Uv95lBIGBk"));
        list.add(createWeekMaterial(30, "Cloud Security Posture Management", "CSPM tools and best practices.", "1SOMqcAUhW8"));
        list.add(createWeekMaterial(31, "Container Security", "Docker and Kubernetes security.", "M8WfSnrIiOc"));
        list.add(createWeekMaterial(32, "DevSecOps", "Integrating security into DevOps pipelines.", "2D9XbfbXwEQ"));
        list.add(createWeekMaterial(33, "Mobile App Security Testing", "Android and iOS app security assessment.", "M8WfSnrIiOc"));
        list.add(createWeekMaterial(34, "ICS/SCADA Security", "Industrial control systems security.", "H6W8v9tTj04"));
        list.add(createWeekMaterial(35, "Threat Modeling", "Identifying and mitigating security risks.", "3Kq1MIfTWCE"));
        list.add(createWeekMaterial(36, "Security Architecture & Design", "Designing secure systems and networks.", "aT7JQvVk4LY"));
        list.add(createWeekMaterial(37, "Purple Teaming", "Collaborative security testing.", "fDeLtKxZgtg"));
        list.add(createWeekMaterial(38, "Secure Cloud-Native Development", "Building secure cloud applications.", "1SOMqcAUhW8"));
        list.add(createWeekMaterial(39, "Review & Assessment", "Review of Q3 topics and knowledge assessment.", ""));

        // Quarter 4: Specialization (Weeks 40-52)
        list.add(createWeekMaterial(40, "Advanced Penetration Testing", "Advanced exploitation techniques.", "3Kq1MIfTWCE"));
        list.add(createWeekMaterial(41, "Cloud Penetration Testing", "Assessing cloud environments.", "1SOMqcAUhW8"));
        list.add(createWeekMaterial(42, "Red Team Operations & Adversary Emulation", "Advanced red teaming.", "fDeLtKxZgtg"));
        list.add(createWeekMaterial(43, "Blue Team Operations & Detection Engineering", "Advanced blue teaming.", "VjYFr2TZwC8"));
        list.add(createWeekMaterial(44, "Threat Intelligence & Hunting", "Advanced threat hunting.", "6JNGvVuwEdw"));
        list.add(createWeekMaterial(45, "Digital Forensics & Incident Response", "Advanced DFIR techniques.", "8tqHjY8mO04"));
        list.add(createWeekMaterial(46, "Malware Analysis & Reverse Engineering", "Advanced malware analysis.", "p1S5hCtttjU"));
        list.add(createWeekMaterial(47, "Secure Software Development Lifecycle", "Building security into SDLC.", "2D9XbfbXwEQ"));
        list.add(createWeekMaterial(48, "Security Leadership & Management", "Security program management.", "aT7JQvVk4LY"));
        list.add(createWeekMaterial(49, "Emerging Threats & Technologies", "AI/ML in security, quantum computing, etc.", "bLV4bBh8aec"));
        list.add(createWeekMaterial(50, "Capstone Project Planning", "Planning your final project.", ""));
        list.add(createWeekMaterial(51, "Capstone Project Implementation", "Working on your final project.", ""));
        list.add(createWeekMaterial(52, "Capstone Project Presentation & Review", "Presenting your work and course wrap-up.", ""));
        
        return list;
    }

    private static Material createWeekMaterial(int week, String title, String description, String videoId) {
        return new Material(UUID.randomUUID().toString(), title, "Week " + week + ": " + description, description, java.util.List.of("https://www.youtube.com/watch?v=" + videoId));
    }
}
