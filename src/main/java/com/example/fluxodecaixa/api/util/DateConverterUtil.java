package com.example.fluxodecaixa.api.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DateConverterUtil {

    public static OffsetDateTime toOffsetDateTime(LocalDate localDate, boolean isEndDate) {

        int hora = 0;
        int minuto = 0;
        int segundo = 0;
        int nanosegundo = 0;

        if (isEndDate) {
            hora = 23;
            minuto = 59;
            segundo = 59;
            nanosegundo = 99;
        }
        ZoneOffset offset = ZoneOffset.UTC;

        return OffsetDateTime.of(localDate,
                LocalTime.of(hora, minuto, segundo, nanosegundo),
                offset);
    }
}
