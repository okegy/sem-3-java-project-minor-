package com.cyberlearn.app.util;

import com.cyberlearn.app.model.Progress;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProgressService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Path HOME_DIR = Path.of(System.getProperty("user.home"), ".cyberlearn");
    private static final Path FILE = HOME_DIR.resolve("progress.json");
    private static final Path LEGACY_FILE = Path.of("data/progress.json");

    private static List<Progress> readAll() throws IOException {
        // Ensure directory exists
        if (!Files.exists(HOME_DIR)) {
            Files.createDirectories(HOME_DIR);
        }

        // Migrate legacy file if needed
        if (!Files.exists(FILE)) {
            if (Files.exists(LEGACY_FILE)) {
                Files.createDirectories(FILE.getParent());
                // Read legacy data then write to new location
                List<Progress> legacy = MAPPER.readValue(LEGACY_FILE.toFile(), new TypeReference<List<Progress>>(){});
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), legacy);
            } else {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), new ArrayList<Progress>());
            }
        }
        return MAPPER.readValue(FILE.toFile(), new TypeReference<List<Progress>>(){});
    }

    private static void writeAll(List<Progress> list) throws IOException {
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), list);
    }

    public static Progress get(String username) throws IOException {
        List<Progress> list = readAll();
        for (Progress p : list) if (p.getUsername().equalsIgnoreCase(username)) return p;
        Progress p = new Progress(username);
        list.add(p);
        writeAll(list);
        return p;
    }

    public static void update(Progress p) throws IOException {
        List<Progress> list = readAll();
        Optional<Progress> ex = list.stream().filter(x -> x.getUsername().equalsIgnoreCase(p.getUsername())).findFirst();
        if (ex.isPresent()) {
            list = list.stream().map(x -> x.getUsername().equalsIgnoreCase(p.getUsername()) ? p : x).collect(Collectors.toList());
        } else {
            list.add(p);
        }
        writeAll(list);
    }

    public static List<Progress> all() throws IOException {
        return readAll();
    }
}
