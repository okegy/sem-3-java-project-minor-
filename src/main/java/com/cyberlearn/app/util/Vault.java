package com.cyberlearn.app.util;

import com.cyberlearn.app.model.PasswordEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Vault {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void saveVault(Path file, String masterPassword, List<PasswordEntry> entries) throws Exception {
        String json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entries);
        String encrypted = CryptoUtil.encryptAESGCM(json, masterPassword);
        Files.writeString(file, encrypted, StandardCharsets.UTF_8);
    }

    public static List<PasswordEntry> loadVault(Path file, String masterPassword) throws Exception {
        if (!Files.exists(file)) return new ArrayList<>();
        String encrypted = Files.readString(file, StandardCharsets.UTF_8);
        String json = CryptoUtil.decryptAESGCM(encrypted, masterPassword);
        return MAPPER.readValue(json, new TypeReference<List<PasswordEntry>>(){});
    }
}
