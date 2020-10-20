// Coordinates the whole process of the brute force attack to AES security algorithm
// In charge of running and joining the multiple threads of attack

// package
package com.company;

// imports
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


// BruteForceAES Class
public class BruteForceAES {
    // Attributes
    private int threadCount = 8;
    //    private String passToGenKeys;
    private int totalIterations = 200000;
    private int keyLen = 256;

    // main method of program
    public static void main(String[] args) {
        System.out.println("---------Initiate AES----------");
        BruteForceAES bf = new BruteForceAES();
        bf.runBruteForce();
    }

    // run method, runs the bruteForce Attack
    public void runBruteForce(){
        // Init timer
        long start = System.currentTimeMillis();
        // Generate thread pool
        ExecutorService  exServ = Executors.newCachedThreadPool();
        for (int iterate = 0; iterate < threadCount; iterate++){
            // both keys must be 32 characters / 32 bytes long
            exServ.execute(new AESThread("DavidMenaMadrid1","aaaaaaaaaaaaaaaa",
                    1000/*Num Rounds Algorithm*/, keyLen, totalIterations/threadCount));
        }
        // shut down threads once they are completed
        exServ.shutdown();
        // join or wait threads
        try {
            boolean threadsReady = exServ.awaitTermination(12, TimeUnit.MINUTES);
            if (threadsReady){
                // End Timer
                long end = System.currentTimeMillis();
                double timeRange = ( end - start );
                // Results
                System.out.println("-------------Results--------------");
                System.out.println("Threads Created: " + threadCount);
                System.out.println("Total iterations: " + totalIterations);
                System.out.println("Average decrypt time: " + String.format("%.4f", (timeRange / (totalIterations))) + "ms");
            }
        } catch(InterruptedException except){
            except.printStackTrace();
        }

    }
}
