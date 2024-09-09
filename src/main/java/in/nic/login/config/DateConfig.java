package in.nic.login.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConfig {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static boolean isValidDate(String date) {
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) { return false; }
    }
}
