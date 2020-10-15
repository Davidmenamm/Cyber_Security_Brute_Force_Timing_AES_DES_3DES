import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BruteForceTimer {
    public static int THREAD_NUM = 250;
    public static int UPPER_BOUND = 100000;
    public static int TIME_LIMIT = 1;
    public static String path = System.getProperty("user.dir") + "/src/files/";
    public static String plainTextFile = "plaintext.txt";
    public static String cipherTextFile = "ciphertext.txt";
    public static String resultsTextFile = "results_3des.txt";

    public static void main(String[] args) {
        String message = getFileContent(path + plainTextFile);

        // Generate random Secret Key
        Integer randInt = getRandomKey();
        String key = randInt.toString();

        // Generate random ivString
        String ivString = getRandomString();

        System.out.println("Secret Key: " + key);
        System.out.println("Random ivString: " + ivString);

        Encrypter desEncrypter = new Encrypter(key, ivString);

        System.out.println("---------3DES Encryption----------");

        String encryptedText = desEncrypter.encrypt(message);

        System.out.println("Original Text: " + message);

        System.out.println("Encrypted Text: " + encryptedText);

        writeInFile(path + cipherTextFile, encryptedText);


        System.out.println("\n\n-------------3DES DEcryption----------");
        startBruteForceAttack(encryptedText, ivString);
    }

    public static void startBruteForceAttack(String encryptedText, String ivString) {
        System.out.println("Starting brute force attack...");
        // Decrypted text
        long startTimeDecrypt = System.currentTimeMillis();

        ExecutorService es = Executors.newCachedThreadPool();

        int threadNum = 0;
        for(int i = 0; i < THREAD_NUM; i++){
            es.execute(new EncrypterThread(encryptedText, ivString));
            threadNum++;
        }
        es.shutdown();

        try {
            boolean threadsFinished = es.awaitTermination(TIME_LIMIT, TimeUnit.HOURS);

            if(threadsFinished) {
                System.out.println("-------------Results--------------");
                System.out.println("Threads Created: " + threadNum);
                System.out.println("Total iterations: " + EncrypterThread.iterations);

                long endTimeDecrypt = System.currentTimeMillis();

                double decryptionTime = ( endTimeDecrypt - startTimeDecrypt );

                System.out.println("Average decrypt time: " + String.format("%.4f", (decryptionTime / (EncrypterThread.iterations))) + "ms");

                String resultsString = "-------------Results--------------" + "\n" + "Threads Created: " + threadNum + "\n" + "Total iterations: " + EncrypterThread.iterations + "\n" + "Average decrypt time: " + String.format("%.4f", (decryptionTime / (EncrypterThread.iterations))) + "ms";
                writeInFile(path + resultsTextFile, resultsString);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Generates random key number
    public static Integer getRandomKey() {
        Random rand = new Random();
        return rand.nextInt(UPPER_BOUND);
    }

    public static String getRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    // Obtiene el contenido de un archivo
    public static String getFileContent(String filePath) {
        StringBuilder sb = new StringBuilder("");

        File myObj = new File(filePath);
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                sb.append(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred while reading the file: " + filePath);
            e.printStackTrace();
            return "";
        }

        return sb.toString();
    }

    // Escribe el mensaje encriptado en un archivo
    public static void writeInFile(String filePath, String text) {
        try (FileWriter myWriter = new FileWriter(filePath)) {
            myWriter.write(text);
        } catch (IOException e) {
            System.err.println("An error occurred while writing in file: " + filePath);
            e.printStackTrace();
        }
    }
}
