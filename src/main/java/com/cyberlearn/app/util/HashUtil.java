package com.cyberlearn.app.util;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

public class HashUtil {

    public static String hash(Path file, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        try (FileInputStream fis = new FileInputStream(file.toFile())) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = fis.read(buf)) != -1) {
                md.update(buf, 0, r);
            }
        }
        return HexFormat.of().formatHex(md.digest());
    }

    public static String hashText(String text, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] out = md.digest(text.getBytes());
        return HexFormat.of().formatHex(out);
    }
}
