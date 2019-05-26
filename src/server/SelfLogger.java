package server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfLogger {

    public static void info(String message) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("hh:mm:ss");
        System.out.println("[" + format.format(date) + " INFO]: " + message);
    }
}
