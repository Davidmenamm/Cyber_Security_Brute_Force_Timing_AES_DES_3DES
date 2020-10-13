import javax.crypto.BadPaddingException;
import java.util.Random;

public class BruteForceTimer {
    public static int BLOCK_NUM = 2;
    public static int UPPER_BOUND = 100000;

    public static void main(String[] args) {
        String message = "Attack at Dawn";

        // Generate random Secret Key
        Random rand = new Random();
        Integer randInt = rand.nextInt(UPPER_BOUND);
        String key = randInt.toString();

        // Generate random ivString
        String ivString = getRandomString();

        System.out.println("Original key: " + key);
        System.out.println("Random ivString: " + ivString);

        Encrypter desEncrypter = new Encrypter(key, ivString);

        System.out.println("---------3DES Encryption----------");

        String encryptedText = desEncrypter.encrypt(message);

        System.out.println("Original Text: " + message);

        System.out.println("Encrypted Text: " + encryptedText);

        System.out.println("\n\n-------------3DES DEcryption----------");

        for(int i = 0; i < BLOCK_NUM; i++){
            new EncrypterThread(encryptedText, ivString).start();
        }
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
}
