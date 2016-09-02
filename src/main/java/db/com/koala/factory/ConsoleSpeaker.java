package db.com.koala.factory;

/**
 * Created by Jeka on 24/08/2016.
 */
public class ConsoleSpeaker implements Speaker {
    @Override
    public void speak(String message) {
        System.out.println(message);
    }
}
