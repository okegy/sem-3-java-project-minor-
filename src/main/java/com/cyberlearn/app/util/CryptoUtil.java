package com.cyberlearn.app.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {
    private static final int IV_LEN = 12; // GCM nonce
    private static final int TAG_LEN = 128; // bits
    private static final int PBKDF2_ITER = 65536;
    private static final int KEY_LEN = 256; // bits

    public static String encryptAESGCM(String plaintext, String password) throws Exception {
        byte[] salt = new byte[16];
        byte[] iv = new byte[IV_LEN];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(salt);
        rnd.nextBytes(iv);

        SecretKeySpec key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LEN, iv));
        byte[] ct = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        ByteBuffer bb = ByteBuffer.allocate(4 + salt.length + 4 + iv.length + ct.length);
        bb.putInt(salt.length).put(salt);
        bb.putInt(iv.length).put(iv);
        bb.put(ct);
        return Base64.getEncoder().encodeToString(bb.array());
    }

    public static String decryptAESGCM(String encoded, String password) throws Exception {
        byte[] data = Base64.getDecoder().decode(encoded);
        ByteBuffer bb = ByteBuffer.wrap(data);
        int saltLen = bb.getInt();
        byte[] salt = new byte[saltLen];
        bb.get(salt);
        int ivLen = bb.getInt();
        byte[] iv = new byte[ivLen];
        bb.get(iv);
        byte[] ct = new byte[bb.remaining()];
        bb.get(ct);

        SecretKeySpec key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LEN, iv));
        byte[] pt = cipher.doFinal(ct);
        return new String(pt, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec deriveKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITER, KEY_LEN);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] key = skf.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }
}
