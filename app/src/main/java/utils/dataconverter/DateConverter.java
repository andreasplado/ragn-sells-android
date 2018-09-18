package utils.dataconverter;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andreas on 18.05.2017.
 */

public class DateConverter {

    public static String convertDateToReadableFormatNoTime(String date){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Locale estoniaLocale = new Locale("et", "ET");
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, estoniaLocale);
        String formattedDate = outputFormat.format(parsedDate);
        return formattedDate;
    }
    public static String convertDateTimeToReadableFormat(String date){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Locale estoniaLocale = new Locale("et", "ET");
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, estoniaLocale);
        String formattedDate = outputFormat.format(parsedDate);
        return formattedDate;
    }

    public static String convertCurrentTimeToReadableFormat(){
        String currentDateTimeString = new SimpleDateFormat("HH:mm").format(new Date());
        return currentDateTimeString;
    }
}
