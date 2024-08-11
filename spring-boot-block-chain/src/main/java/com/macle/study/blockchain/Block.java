package com.macle.study.blockchain;

import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Data
public class Block {

    private int index;
    private long timestamp;
    private String data;
    private String previousHash;
    private String hash;

    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = index + timestamp + data + previousHash;
        return applySha256(input);
    }

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters and setters

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}