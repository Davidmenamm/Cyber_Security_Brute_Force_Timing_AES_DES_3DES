package com.eamt.crypto;


import java.util.ArrayList;
import java.util.List;


/**
 * @author EAMT on 13/10/2020
 */
public class DesBruteForceDecryptor {

    private final long instances; // num of instances to create
    private final long repetitions; // num of repetitions for each instance
    private final byte[] cipherText; // ciphertext to decrypt
    private final List<DesDecryptTask> desDecryptTasksList; // A list for DesDecryptTask
    private final long iterations;

    private double averageTime;


    public DesBruteForceDecryptor(long iterations, byte[] cipherText) {
        this.iterations = iterations;
        //this.instances = iterations/250;
        this.instances = iterations/5000;
        this.repetitions = iterations/instances;
        this.cipherText = cipherText;

        this.desDecryptTasksList = new ArrayList<>();

        System.out.printf("\n%d iterations, so create %d instances. Each instance execute %d repetitions (decryption)\n\n",
                iterations, this.instances, this.repetitions);
    }


    public void startBruteForceAttack() {
        long startTime = System.nanoTime();

        // create instances
        DesDecryptTask desDecryptTask;
        for ( int i = 0; i < instances; i++ ) {
            desDecryptTask = new DesDecryptTask( cipherText, repetitions );
            desDecryptTasksList.add( desDecryptTask );

            desDecryptTask.start();
            System.out.println(desDecryptTask.toString() + " started.");

            try {
                desDecryptTask.join();
              //  System.out.println("Try to join " + desDecryptTask.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //totalAverageTime += desDecryptTask.getAverageTime();
            //desDecryptTask.start();
        }

        long endTime = System.nanoTime();
        averageTime = ((double)(endTime - startTime)/iterations) / 1_000_000_000.0;
        System.out.printf("\nAVERAGE TIME (average of averages): %f (s)", (averageTime));
    }

    public double getAverageTime() {
        return averageTime;
    }
}
