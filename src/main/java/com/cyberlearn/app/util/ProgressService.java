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
    private static final Path FILE = Path.of("data/progress.json");

    private static List<Progress> readAll() throws IOException {
        if (!Files.exists(FILE)) {
            Files.createDirectories(FILE.getParent());
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), new ArrayList<Progress>());
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
