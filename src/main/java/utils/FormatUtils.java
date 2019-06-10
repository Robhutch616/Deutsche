package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class FormatUtils {

    public static String removeWhitespace(String data) {
        if (data == null) {
            return null;
        }
        return data.replaceAll("\\s", "");
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";


    public static Date toDate(String dateString) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
