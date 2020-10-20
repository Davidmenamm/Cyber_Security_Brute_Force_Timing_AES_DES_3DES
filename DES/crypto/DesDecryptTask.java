// https://stackoverflow.com/questions/33854058/why-is-my-multithreaded-program-executing-sequentially
package com.eamt.crypto;


import javax.crypto.SecretKey;
import java.util.*;


/**
 * @author EAMT on 13/10/2020
 */
public class DesDecryptTask extends Thread {

    private static int TASKS_COUNTER = 0; // Counts the task created

    private double averageTime; // Average time of the task
    private final byte[] ciphertext; // Bytes to decrypt
    private final long repetitions; // Num of repetitions for instance
    private final DesCipher desCipher; // A DES cipher
    private SecretKey secretKey;

    private final String desDecryptTaskName; // Name for class


    public DesDecryptTask(byte[] cipherText, long repetitions) {
        this.ciphertext = cipherText;
        this.repetitions = repetitions;
        this.desCipher = new DesCipher();

        TASKS_COUNTER++;
        desDecryptTaskName = "DesDecryptTask " + TASKS_COUNTER + " - " + hashCode();
    }


    private void doDecryptionTask() {
        byte[] decrypted;
        for ( int i = 0; i < repetitions; i++ ) {
            //System.out.println("Decryption started by " + desDecryptTaskName);
            secretKey = desCipher.generateDesSecretKey();  // Create a DES secret key each repetition
            decrypted = desCipher.decrypt( secretKey, this.ciphertext );
            //System.out.println(bytes2String(decrypted));

            //System.out.printf("Key used: %s\n", secretKey2String(secretKey));
        }


        /*System.out.println("Total time: " + totalTime);
        System.out.println("Average time: " + averageTime);*/
    }


    public double getAverageTime() {
        return averageTime;
    }


    @Override
    public void run() {
        //long startTime = System.nanoTime();
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        doDecryptionTask();

        //long endTime = System.nanoTime();
        //double execTime = ( endTime - startTime ) / 1_000_000_000.0;
        //this.averageTime = execTime / repetitions;

        //System.out.printf("%s Done. Average time %f (s), executing %d repetitions.\n",
          //      this.toString(), getAverageTime(), repetitions);
    }


    @Override
    public String toString() {
        return this.desDecryptTaskName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DesDecryptTask that = (DesDecryptTask) o;
        return repetitions == that.repetitions &&
                Double.compare(that.averageTime, averageTime) == 0 &&
                Arrays.equals(ciphertext, that.ciphertext) &&
                Objects.equals(desCipher, that.desCipher) &&
                Objects.equals(desDecryptTaskName, that.desDecryptTaskName) &&
                Objects.equals(secretKey, that.secretKey);
    }

    
    @Override
    public int hashCode() {
        int result = Objects.hash(repetitions, desCipher, averageTime, desDecryptTaskName, secretKey);
        result = 31 * result + Arrays.hashCode(ciphertext);
        return result;
    }

}
