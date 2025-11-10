package com.cyberlearn.app.util;

import com.cyberlearn.app.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

public class AuthService {
    private static String hash(String text) throws Exception {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] out = md.digest(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return java.util.HexFormat.of().formatHex(out);
    }
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Path USERS = Path.of(System.getProperty("user.home"), ".cyberlearn", "users.json");
    private static User current;

    public static User getCurrent(){ return current; }

    public static void ensureAdmin() throws Exception {
        if (!Files.exists(USERS)) {
            // Create parent directories if they don't exist
            Files.createDirectories(USERS.getParent());
            
            // Create initial admin user
            List<User> init = new ArrayList<>();
            init.add(new User("admin", hash("admin123"), "ADMIN", true));
            
            // Write to file
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(USERS.toFile(), init);
            
            System.out.println("Created admin user at: " + USERS.toAbsolutePath());
        }
    }

    public static boolean register(String username, String password) throws Exception {
        List<User> users = read();
        if (users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) return false;
        users.add(new User(username, hash(password), "STUDENT", true));
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(USERS.toFile(), users);
        return true;
    }

    public static boolean login(String username, String password) {
        try {
            List<User> users = read();
            Optional<User> u = users.stream()
                .filter(x -> x.getUsername().equalsIgnoreCase(username))
                .findFirst();
                
            if (u.isPresent() && u.get().getPasswordHash().equals(hash(password)) && u.get().isActive()) {
                current = u.get();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> read() throws Exception {
        ensureAdmin();
        List<User> users = MAPPER.readValue(USERS.toFile(), new TypeReference<List<User>>(){});

        // Merge any usernames that exist in progress data but are missing from users.json
        try {
            java.util.List<com.cyberlearn.app.model.Progress> progressList = com.cyberlearn.app.util.ProgressService.all();
            boolean changed = false;
            for (com.cyberlearn.app.model.Progress p : progressList) {
                boolean exists = users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(p.getUsername()));
                if (!exists) {
                    // Add as active STUDENT with a temporary password; admin can reset it
                    users.add(new User(p.getUsername(), hash("changeme123"), "STUDENT", true));
                    changed = true;
                }
            }
            if (changed) {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(USERS.toFile(), users);
            }
        } catch (Exception ignored) {}

        return users;
    }

    public static String hash_old(String text) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] out = md.digest(text.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(out);
    }

    public static void resetPassword(String username, String newPassword) throws Exception {
        java.util.List<User> users = read();
        for (User u : users){
            if (u.getUsername().equalsIgnoreCase(username)){
                u.setPasswordHash(hash(newPassword));
            }
        }
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(USERS.toFile(), users);
    }

    public static boolean toggleActive(String username) throws Exception {
        java.util.List<User> users = read();
        boolean active = true;
        for (User u : users){
            if (u.getUsername().equalsIgnoreCase(username)){
                u.setActive(!u.isActive());
                active = u.isActive();
            }
        }
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(USERS.toFile(), users);
        return active;
    }
}
