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
    public AESThread(String targetKey, String searchKey, int numIterationsAlg, int keyLen, int numOfRuns) {
        this.targetKey = targetKey;
        this.searchKey = searchKey; // 16 bytes o 128 bits
        this.numIterationsAlg = numIterationsAlg;
        this.keyLen = keyLen;
        this.numOfRuns = numOfRuns;
        this.salt = generateSalt();
    }

    // Attributes
    // To generate a password-based encryption (PBE), based on user input
    private String targetKey; // password from which to generate or decrypt the keys
    private String searchKey;
    private String salt; // to prevent dictionary attacks
    private int numIterationsAlg; // adds rounds to algorithm, making it more secure, but computationally heavier
    private int keyLen;
    private int numOfRuns;
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
    public String getSalt() {
        return salt;
    }
    public int getNumIterationsAlg() {
        return numIterationsAlg;
    }
    public int getKeyLen() {
        return keyLen;
    }
    public byte[] getIv() {
        return iv;
    }
    public int getNumOfRuns() {
        return numOfRuns;
    }

    // Setter Methods
    public void setSalt() { // random
        this.salt = generateSalt();
    }
    public void setNumIterationsAlg(int numIterationsAlg) {
        this.numIterationsAlg = numIterationsAlg;
    }
    public void setKeyLen(int keyLen) {
        this.keyLen = keyLen;
    }
    public void setIv(byte[] iv) {
        this.iv = iv;
    }
    public void setNumOfRuns(int numOfRuns) {
        this.numOfRuns = numOfRuns;
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
        SecretKeyFactory skfact = SecretKeyFactory.getInstance(
                this.secretKeyFactoryAlgorithm);
        PBEKeySpec keySpec = new PBEKeySpec(this.targetKey.toCharArray(),
                saltBytes, this.numIterationsAlg, this.keyLen);
        SecretKey secretKey = skfact.generateSecret(keySpec);
        SecretKeySpec formatKey = new SecretKeySpec(secretKey.getEncoded(),
                algorithmName);
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
    public String decrypt(String cipherText) throws Exception {
        byte[] saltBytes = salt.getBytes("UTF-8");
        byte[] encryptTextBytes = new Base64().decode(cipherText);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(
                this.secretKeyFactoryAlgorithm);
        PBEKeySpec spec = new PBEKeySpec(this.searchKey.toCharArray(),
                saltBytes, this.numIterationsAlg, this.keyLen);
        SecretKey secretKey = skf.generateSecret(spec);
        SecretKeySpec formatKey = new SecretKeySpec(secretKey.getEncoded(),
                algorithmName);
        //decrypt the message
        Cipher cipher = Cipher.getInstance(blockType);
        cipher.init(Cipher.DECRYPT_MODE, formatKey, new IvParameterSpec(iv));

        byte[] decyrptTextBytes = null;
        try {
            decyrptTextBytes = cipher.doFinal(encryptTextBytes);
//            System.out.println("decyrptTextBytes ");
//            System.out.println(decyrptTextBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("It is a wrong key");
        }
        String text = new String(decyrptTextBytes);
        return text;
    }

    /**
     * Increment key + 1
     */
    private void incrementKey(){
        // turn string to char array
        char[] charArray = this.searchKey.toCharArray();
        // turn each int into corresponding bits String
        int n = charArray.length;
        for (int i = 0; i < n; i++){
            int numCh = charArray[i]; // each char as num
            numCh = (((numCh-97) + 1) % 26)+97;
            charArray[i] = (char) numCh; // (-97) in range of lower letters
        }
        // update search key
        String newKey = new String(charArray);
        this.searchKey = newKey;
        System.out.println("Llave out ");
        System.out.println(newKey);
        System.out.println("Llave out 2");
    }

    /**
     * Main function runned in the thread
     * Iterates through the brute force attack decryptions of current thread
     */
    public void run(){
        // Encrypt
        String cipherText = "";
        try{
            cipherText = this.encrypt(this.targetKey);
        } catch(Exception except){
                except.printStackTrace();
                System.out.println("except.printStackTrace()");
        }

        System.out.println("cipherText");
        System.out.println(cipherText);
        System.out.println("Llave in");
        System.out.println(this.searchKey);
        // run decryptions
        for (int i = 0; i < this.numOfRuns; i++){
            try{
                this.decrypt(cipherText);
            } catch(Exception except){
                except.printStackTrace();
            }
            incrementKey();
        }
    }

}
