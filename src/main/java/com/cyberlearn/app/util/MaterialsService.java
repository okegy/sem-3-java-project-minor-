package com.cyberlearn.app.util;

import com.cyberlearn.app.model.Material;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        list.add(new Material(UUID.randomUUID().toString(), "Password Safety & MFA", "L1 Basics",
                "Strong passwords (12+), avoid reuse, enable MFA; demo with the Password Manager.",
                java.util.List.of("https://haveibeenpwned.com/Passwords", "https://www.cisa.gov/secure-our-world/use-strong-passwords")));
        list.add(new Material(UUID.randomUUID().toString(), "Networks & Ports", "L2 Network",
                "Ports as service doors, common ports (80,443,22), scanning ethics; practice with Port Scanner.",
                java.util.List.of("https://nmap.org/book/man-port-scanning-techniques.html", "https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers")));
        list.add(new Material(UUID.randomUUID().toString(), "AES & Secure Sharing", "L3 Crypto",
                "Symmetric crypto, AES basics, key handling; encrypt/decrypt text and share.",
                java.util.List.of("https://cryptopals.com", "https://en.wikipedia.org/wiki/Advanced_Encryption_Standard")));
        list.add(new Material(UUID.randomUUID().toString(), "Hashes & Integrity", "L4 Integrity",
                "SHA-256 vs MD5, collisions, verifying downloads; check file fingerprints.",
                java.util.List.of("https://www.rfc-editor.org/rfc/rfc6234", "https://docs.python.org/3/library/hashlib.html")));
        list.add(new Material(UUID.randomUUID().toString(), "Incident Basics", "L5 Forensics",
                "Detecting suspicious changes, triage checklist; combine all tools in a mini-lab.",
                java.util.List.of("https://owasp.org/www-project-top-ten/", "https://www.sans.org/")));
        return list;
    }
}
