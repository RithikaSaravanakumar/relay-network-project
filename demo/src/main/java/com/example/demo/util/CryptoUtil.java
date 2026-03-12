package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * Utility class for cryptographic operations
 * Generates RSA key pairs and handles public key serialization
 */
@Component
public class CryptoUtil {

    private static final int KEY_SIZE = 2048;
    private static final String ALGORITHM = "RSA";

    /**
     * Generate RSA Key Pair
     * @return KeyPair containing public and private keys
     * @throws NoSuchAlgorithmException if RSA algorithm is not available
     */
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Encode RSA PublicKey to Base64 String
     * @param publicKey the public key to encode
     * @return Base64 encoded string representation of the public key
     */
    public String encodePublicKey(PublicKey publicKey) {
        byte[] publicKeyBytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }

    /**
     * Encode RSA PrivateKey to Base64 String
     * @param privateKey the private key to encode
     * @return Base64 encoded string representation of the private key
     */
    public String encodePrivateKey(PrivateKey privateKey) {
        byte[] privateKeyBytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(privateKeyBytes);
    }

    /**
     * Decode Base64 encoded PublicKey string back to PublicKey
     * @param encodedPublicKey Base64 encoded public key string
     * @return PublicKey object
     * @throws Exception if decoding or key generation fails
     */
    public PublicKey decodePublicKey(String encodedPublicKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(encodedPublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static String extractPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

}
