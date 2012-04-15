package fuzzydatediff;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SimpleFuzzyDateDiff implements FuzzyDateDiff {

  @Override
  public String diff(String referenceDate, String actualDate) {
    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    try {
      Calendar referenceCalendar = Calendar.getInstance();
      referenceCalendar.setTime(dateFormat.parse(referenceDate));
      Calendar actualCalendar = Calendar.getInstance();
      actualCalendar.setTime(dateFormat.parse(actualDate));

      Map<CalendarField, Long> diffMap = new HashMap<>();
      
      long refMilli = referenceCalendar.getTimeInMillis();
      long actualMilli = actualCalendar.getTimeInMillis();
      
      diffMap.put(CalendarField.NOW, (long) 0);

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
          CalendarField.YEAR,
          CalendarField.MONTH,
          CalendarField.DAY,
          CalendarField.HOUR,
          CalendarField.MIN,          
          CalendarField.SEC,          
      };
      
      CalendarField selectedCalendarField = CalendarField.NOW;
      for (CalendarField cf : calendarFields) {
        Long d = diffMap.get(cf);
        if (d != 0) {
          selectedCalendarField = cf;          
          break;
        }
      }
      if (actualMilli >= refMilli) {
        return futureString(selectedCalendarField, diffMap.get(selectedCalendarField));
      }
      else {
        return pastString(selectedCalendarField, diffMap.get(selectedCalendarField));
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String pastString(CalendarField selectedCalendarField, Long diff) {
    if (selectedCalendarField == CalendarField.NOW) {
      return "Just now";
    }
    else {
      return diff+" "+selectedCalendarField.name+
          (diff > 1 ? "s ": " ")+"ago";
    }
  }

  private static String futureString(CalendarField selectedCalendarField, Long diff) {
    if (selectedCalendarField == CalendarField.NOW) {
      return "Just now";
    }
    else {
      return "In "+diff+" "+selectedCalendarField.name+
          (diff > 1 ? "s ": " ");
    }
  }

  public static void main(String[] args) {
    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    FuzzyDateDiff fdd = new SimpleFuzzyDateDiff();
    Calendar r = Calendar.getInstance();
    r.set(2012, 3, 23, 2, 23, 12);

    Calendar a = Calendar.getInstance();
    a.set(2013, 3, 12, 13, 35, 2);
    String dString =
        fdd.diff(dateFormat.format(r.getTime()), dateFormat.format(a.getTime()));
    System.out.println(dString);
  }

}
