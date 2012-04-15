package fuzzydatediff;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SimpleFuzzyDateDiff implements FuzzyDateDiff {

  @Override
  public String diff(String referenceDate, String actualDate,
      String dateTimePattern) {
    DateFormat dateFormat = new SimpleDateFormat(dateTimePattern);
    try {
      Calendar referenceCalendar = Calendar.getInstance();
      referenceCalendar.setTime(dateFormat.parse(referenceDate));
      Calendar actualCalendar = Calendar.getInstance();
      actualCalendar.setTime(dateFormat.parse(actualDate));

      Map<CalendarField, Long> diffMap = new HashMap<>();

      long refMilli = referenceCalendar.getTimeInMillis();
      long actualMilli = actualCalendar.getTimeInMillis();

      diffMap.put(CalendarField.NOW, (long) 1);

      long timeDiff = Math.abs(actualMilli - refMilli) / 1000; // in seconds
      diffMap.put(CalendarField.SEC, timeDiff % 60);
      timeDiff /= 60; // in mins
      diffMap.put(CalendarField.MIN, timeDiff % 60);
      timeDiff /= 60; // in hours
      diffMap.put(CalendarField.HOUR, timeDiff % 24);
      timeDiff /= 24; // in days
      diffMap.put(CalendarField.DAY, (long) (timeDiff % 30.41));
      timeDiff /= 30.41; // in months
      diffMap.put(CalendarField.MONTH, timeDiff % 12);
      timeDiff /= 12; // in years
      diffMap.put(CalendarField.YEAR, timeDiff);

      final CalendarField[] calendarFields = new CalendarField[] {
          CalendarField.YEAR, CalendarField.MONTH, CalendarField.DAY,
          CalendarField.HOUR, CalendarField.MIN, CalendarField.SEC,
          CalendarField.NOW, };

      CalendarField selectedCalendarField = CalendarField.NOW;
      CalendarField nextCalendarField = CalendarField.NOW;
      for (int i = 0; i < calendarFields.length - 1; i++) {
        Long d = diffMap.get(calendarFields[i]);
        if (d != 0) {
          selectedCalendarField = calendarFields[i];
          nextCalendarField = calendarFields[i + 1];
          break;
        }
      }

      if (selectedCalendarField == CalendarField.NOW) {
        return "Just now";
      } else {
        String s = concat(
            diffToString(selectedCalendarField,
                diffMap.get(selectedCalendarField)),
            diffToString(nextCalendarField, diffMap.get(nextCalendarField)));

        return (actualMilli >= refMilli) ? futureString(s) : pastString(s);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static String pastString(String s) {
    return s + " ago";
  }

  private static String futureString(String s) {
    return "In " + s;
  }

  private static String diffToString(CalendarField calendarField, Long diff) {
    if (calendarField != CalendarField.NOW && diff > 0)
      return diff + " " + calendarField.name + (diff > 1 ? "s" : "");
    return "";
  }

  private static String concat(String s1, String s2) {
    return s1 + "" + (s2.equals("") ? "" : " and " + s2);
  }
}
