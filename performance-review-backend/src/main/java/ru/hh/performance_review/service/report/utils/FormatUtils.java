package ru.hh.performance_review.service.report.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class FormatUtils {

    public static String getLocalDateFormatIsoDate(LocalDate localDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return localDate.format(dateTimeFormatter);
    }
}
