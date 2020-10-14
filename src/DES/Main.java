package com.eamt;


import com.eamt.crypto.DesBruteForceDecryptor;
import com.eamt.crypto.DesCipher;
import com.eamt.crypto.DesDecryptTask;
import sun.security.krb5.internal.crypto.Des;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        DesCipher desCipher = new DesCipher();
        SecretKey secretKey = desCipher.generateDesSecretKey(); // Create a DES key


        // Plain text
        byte[] text1 = "No body can see me!".getBytes(StandardCharsets.UTF_8);
        byte[] text2 = getFileContent("_plaintext.txt").getBytes(StandardCharsets.UTF_8);

        // Cipher text
        byte[] cipherText = desCipher.encrypt( secretKey, text2 );
        System.out.print("Ciphertext: ");
        if (cipherText != null) {
            //System.out.println(DesCipher.bytes2String(cipherText));
            writeInFile("_ciphertext.txt", DesCipher.bytes2String(cipherText));
        }

        // Decrypted text
        long startTimeDecrypt = System.nanoTime();
        byte[] textDecrypted = desCipher.finalDecrypt( secretKey, cipherText );
        long endTimeDecrypt = System.nanoTime();
        double decryptionTime = ( endTimeDecrypt - startTimeDecrypt ) / 1_000_000_000.0;

        System.out.print("\nDecrypted text: ");
        if (textDecrypted != null) {
            //System.out.println(DesCipher.bytes2String(textDecrypted));
            writeInFile("_decryptedtext.txt", DesCipher.bytes2String(textDecrypted));
        }
        System.out.println("\tSingle Decryption time: " + decryptionTime);


        // Create Bruteforce
        //DesBruteForceDecryptor bruteforce = new DesBruteForceDecryptor( 200000, cipherText );
        DesBruteForceDecryptor bruteforce = new DesBruteForceDecryptor( 10000, cipherText );
        bruteforce.startBruteForceAttack();
        double averageTime = bruteforce.getAverageTime();

        System.out.println("\nAverage time: " + averageTime);

        System.out.println("\n\nTotal execution time " +
                ((double)(System.nanoTime() - startTime)/1_000_000_000.0) + "(s)");


        //executeTest();
        //threadTest(cipherText);
    }


    public static void threadTest(byte[] cipherText) {
        DesDecryptTask desDecryptTask = new DesDecryptTask( cipherText, 10 );
        DesDecryptTask desDecryptTask1 = new DesDecryptTask( cipherText, 10 );
        DesDecryptTask desDecryptTask2 = new DesDecryptTask( cipherText, 10 );

        Thread task = new Thread(desDecryptTask, desDecryptTask.toString());
        Thread task1 = new Thread(desDecryptTask1, desDecryptTask1.toString());
        Thread task2 = new Thread(desDecryptTask2, desDecryptTask1.toString());

        task.start();
        task1.start();
        task2.start();

    }


    public static void executeTest() {
        DesCipher desEncryptorCipher = new DesCipher();
        SecretKey secretKey = desEncryptorCipher.generateDesSecretKey(); // Create a DES key
        Cipher desCipher = DesCipher.getInstance(); // Create a DES cipher

        // Plain text
        byte[] text = "No body can see me!".getBytes(StandardCharsets.UTF_8);

        // Cipher text
        byte[] cipherText = desEncryptorCipher.encrypt( secretKey, text );
        System.out.println("Encrypted:");
        System.out.println(Arrays.toString(cipherText));
        if (cipherText != null) {
            System.out.println(DesCipher.bytes2String(cipherText));
        }

        // Decrypted text
        byte[] textDecrypted = desEncryptorCipher.decrypt( secretKey, cipherText );
        System.out.println("\nDecrypted:");
        System.out.println(Arrays.toString(textDecrypted));
        if (textDecrypted != null) {
            System.out.println(DesCipher.bytes2String(textDecrypted));
        }
    }


    /**
     * Reads a file and returns its content as String
     * @param pathname String
     * @return String
     */
    public static String getFileContent(String pathname) {
        StringBuilder sb = new StringBuilder("");

        File myObj = new File(pathname);
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                sb.append(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred while reading the file: " + pathname);
            e.printStackTrace();
            return "";
        }

        return sb.toString();
    }


    /**
     * Writes in a file the given text
     * @param fileName String
     * @param text String
     */
    public static void writeInFile(String fileName, String text) {
        // Use try resource (open and close the FileWriter)
        try (FileWriter myWriter = new FileWriter(fileName)) {
            myWriter.write(text);
        } catch (IOException e) {
            System.err.println("An error occurred while writing in file: " + fileName);
            e.printStackTrace();
        }
    }

}
