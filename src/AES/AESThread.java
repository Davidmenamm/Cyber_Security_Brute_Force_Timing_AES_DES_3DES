// Represents a thread part of the BruteForce Attack to AES security algorithm

// package
package com.company;

// imports
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

// EncryptionThread Class
public class AESThread extends Thread{
    // Create Salt
    private String generateSalt(){
        SecureRandom rand = new SecureRandom();
        byte bytes[] = new byte[20]; // byte[16];// same size as AES blocks, 128 bits or 16 bytes
        rand.nextBytes(bytes);
        String txt = new String(bytes);
        return txt;
    }

    // Constructor
    public AESThread(String targetKey, String searchKey, int numIterations, int keyLen) {
        this.targetKey = targetKey;
        this.searchKey = searchKey; // 16 bytes o 128 bits
        this.mainKey = mainKey;
        this.numIterations = numIterations;
        this.keyLen = keyLen;
        this.salt = generateSalt();
    }

    // Attributes
    // To generate a password-based encryption (PBE), based on user input
    private String targetKey;
    private String searchKey;
    private String mainKey; // password from which to generate the keys
    private String salt; // to prevent dictionary attacks
    private int numIterations; // adds rounds to algorithm, making it more secure, but computationally heavier
    private int keyLen;
    private byte[] iv; // initial vector of bytes, later implemented
    // Selecting type of encryption and details
    private String algorithmName= "AES";
    private String blockType = "AES/CBC/PKCS5Padding"; // defines block size // AES/CBC/PKCS7Padding
    private String secretKeyFactoryAlgorithm = "PBKDF2WithHmacSHA1"; // hash function, aids in the key development


    // Getter Methods
    public String getAlgorithmName() {
        return algorithmName;
    }
    public String getBlockType() {
        return blockType;
    }
    public String getSecretKeyFactoryAlgorithm() {
        return secretKeyFactoryAlgorithm;
    }
    public String getmainKey() {
        return mainKey;
    }
    public String getSalt() {
        return salt;
    }
    public int getNumIterations() {
        return numIterations;
    }
    public int getKeyLen() {
        return keyLen;
    }
    public byte[] getIv() {
        return iv;
    }

    // Setter Methods
    public void setmainKey(String plainText) {
        this.mainKey = plainText;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }
    public void setKeyLen(int keyLen) {
        this.keyLen = keyLen;
    }
    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    // Methods:
    /**
     * @param plainText
     * @return encryptedText
     * @throws Exception
     */
    public String encrypt(String plainText) throws Exception{
        // Generate the key
        byte[] saltBytes = salt.getBytes("UTF-8");
        SecretKeyFactory skfact = SecretKeyFactory.getInstance(this.secretKeyFactoryAlgorithm);
        PBEKeySpec keySpec = new PBEKeySpec(this.targetKey.toCharArray(), saltBytes, this.numIterations, this.keyLen);
        SecretKey secretKey = skfact.generateSecret(keySpec);
        SecretKeySpec formatKey = new SecretKeySpec(secretKey.getEncoded(), algorithmName);
        // Start AES for encryption
        Cipher cipher = Cipher.getInstance(blockType);
        cipher.init(Cipher.ENCRYPT_MODE, formatKey); // is it missing the iv?
        //Generate initial vector
        this.iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedText = cipher.doFinal(plainText.getBytes("UTF-8"));
        // return
        return new Base64().encodeAsString(encryptedText);
    }
    /**
     * @param cipherText
     * @return decrypted text
     * @throws Exception
     */
    public String decrypt(String cipherText, String inputKey) throws Exception {
        byte[] saltBytes = salt.getBytes("UTF-8");
        byte[] encryptTextBytes = new Base64().decode(cipherText);

        SecretKeyFactory skf = SecretKeyFactory.getInstance(this.secretKeyFactoryAlgorithm);
        PBEKeySpec spec = new PBEKeySpec(this.searchKey.toCharArray(), saltBytes, this.numIterations, this.keyLen);
        SecretKey secretKey = skf.generateSecret(spec);
        SecretKeySpec formatKey = new SecretKeySpec(secretKey.getEncoded(), algorithmName);

        //decrypt the message
        Cipher cipher = Cipher.getInstance(blockType);
        cipher.init(Cipher.DECRYPT_MODE, formatKey, new IvParameterSpec(iv));

        byte[] decyrptTextBytes = null;
        try {
            decyrptTextBytes = cipher.doFinal(encryptTextBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String text = new String(decyrptTextBytes);
        return text;
    }

    /** En proceso ...
     * Falta implementar el metodo run con las iteraciones respectivas de descencripción
     * Por último, poner como variables algunos parámetros en el código que se dejaron quemados para fines de pruebas
     */
}
