import javax.crypto.BadPaddingException;
import java.util.Random;

public class EncrypterThread extends Thread{
    private String text;
    private String ivString;

    public static int ITERATION_NUM = 2;
    public static int UPPER_BOUND = 100000;

    public EncrypterThread (String text, String ivString){
        this.text = text;
        this.ivString = ivString;
    }

    public void run(){
        System.out.println(getName()  + " is running...");
        //  Generates random key
        Random rand = new Random();
        Integer randIntKey = rand.nextInt(UPPER_BOUND);
        String randKey = randIntKey.toString();

        for (int i = 0; i < ITERATION_NUM; i++){
            System.out.println("Random Key: " + randIntKey);

            Encrypter desEncrypter = new Encrypter(randKey, ivString);
            String originalText = desEncrypter.decrypt(getText());

            System.out.println("Encrypted Text: " + getText());

            System.out.println("Original Text: " + originalText);
            randIntKey++;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
