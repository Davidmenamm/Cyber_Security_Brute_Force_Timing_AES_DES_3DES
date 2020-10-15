package des;

import java.util.Random;

public class EncrypterThread extends Thread{
    private String text;
    private String ivString;

    public static int ITERATION_NUM = 800;
    public static int UPPER_BOUND = 100000;
    public static int iterations = 0;

    public EncrypterThread (String text, String ivString){
        this.text = text;
        this.ivString = ivString;
    }

    public void run(){
        System.out.println(getName()  + " is running...");

        //  Generates random key
        Integer randIntKey = getRandomKey();
        String randKey = randIntKey.toString();

        for (int i = 0; i < ITERATION_NUM; i++){
//            System.out.println("Random Key: " + randIntKey);

            Encrypter desEncrypter = new Encrypter(randKey, ivString);
            String originalText = desEncrypter.decrypt(getText());

//            System.out.println("Encrypted Text: " + getText());
//
//            System.out.println("Original Text: " + originalText);
            randIntKey++;
            iterations++;
        }
    }

    public static Integer getRandomKey() {
        Random rand = new Random();
        return rand.nextInt(UPPER_BOUND);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
