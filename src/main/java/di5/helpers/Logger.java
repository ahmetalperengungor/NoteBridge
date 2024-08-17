package di5.helpers;

public class Logger {
    public static void error(String message) {
        System.out.println("NOTEBRIDGE ERROR: " + message);
    }

    public static void log(String message) {
        System.out.println("NOTEBRIDGE: " + message);
    }
}
