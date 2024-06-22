package es.uji.ei1027.TotFest.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class StringToTimeConverter implements Converter<String, Time> {
    private static final String TIME_FORMAT = "HH:mm";

    @Override
    public Time convert(String source) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
            timeFormat.setLenient(false);
            long ms = timeFormat.parse(source).getTime();
            return new Time(ms);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid time format. Please use this pattern: " + TIME_FORMAT);
        }
    }
}
