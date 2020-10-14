package com.eamt.crypto;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author EAMT on 13/10/2020
 */
public class DesCipher {

    private static KeyGenerator keyGenerator = null;
    private final Cipher desCipher;


    /**
     * Initializes the Cipher and a keyGenerator for DES Algorithm
     */
    public DesCipher(){
        desCipher = DesCipher.getInstance();

        try {
            keyGenerator = KeyGenerator.getInstance( "DES" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public byte[] finalDecrypt ( SecretKey secretKey, byte[] cipherText ) {
        try {
            if ( desCipher != null && cipherText != null ) {
                desCipher.init( Cipher.DECRYPT_MODE, secretKey );
                return desCipher.doFinal( cipherText );
            }
            else {
                System.out.println( "Cannot decrypt because null was received." );
                return null;
            }
        } catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException e ) {
            System.err.println( "Error while trying to encrypt." );
            e.printStackTrace();
            return null;
        }
    }


    public byte[] decrypt( SecretKey secretKey, byte[] cipherText ) {
        try {
            if ( desCipher != null && cipherText != null ) {
                desCipher.init( Cipher.DECRYPT_MODE, secretKey );
                return desCipher.update( cipherText );
            }
            else {
                System.out.println( "Cannot decrypt because null was received." );
                return null;
            }
        } catch ( InvalidKeyException e ) {
            System.err.println( "Error while trying to encrypt." );
            e.printStackTrace();
            return null;
        }
    }


    public byte[] encrypt( SecretKey secretKey, byte[] textBytes ) {
        try {
            if ( desCipher != null ) {
                desCipher.init( Cipher.ENCRYPT_MODE, secretKey );
                return desCipher.doFinal( textBytes );
            }
            else {
                System.out.println( "Cannot encrypt because null was received." );
                return null;
            }
        } catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException e ) {
            System.err.println( "Error while trying to encrypt." );
            e.printStackTrace();
            return null;
        }
    }



    /**
     * A DES (Data Encryption Standard) cipher in Electronic Codebook mode, with PKCS #5-style padding.
     * @return Cipher
     */
    public static Cipher getInstance() {
        try {
            return Cipher.getInstance( "DES/ECB/PKCS5Padding" );
        } catch ( NoSuchAlgorithmException | NoSuchPaddingException e  ) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Returns a SecretKey object for DES Algorithm
     * @return SecretKey
     */
    public SecretKey generateDesSecretKey(){
        return keyGenerator.generateKey();
    }


    /**
     * Converts a bytes array into a String
     * @param bytes byte[]
     * @return String
     */
    public static String bytes2String( byte[] bytes ) {
        StringBuilder sb = new StringBuilder();
        for ( byte b : bytes ) {
            sb.append( (char) b );
        }
        return sb.toString();
    }


    /**
     * Returns a decoded SecretKey object as String
     * @param secretKey SecretKey
     * @return String
     */
    public String secretKey2String(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }


    @Override
    public String toString() {
        return "DES Encryptor as " + super.toString();
    }

}
