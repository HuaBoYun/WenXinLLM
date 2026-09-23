package com.huabo.fxgl.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class DateConverter implements Converter<String, LocalDateTime> {
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /*public static LocalDateTime strToLocalDateTime(String data, String format){
        return LocalDate.parse(data, DateTimeFormatter.ofPattern(format)).atStartOfDay();
//        return LocalDateTime.parse(data, DateTimeFormatter.ofPattern(format));
    }*/
    @Override
    public LocalDateTime convert(String source) {
        return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
    }
}
