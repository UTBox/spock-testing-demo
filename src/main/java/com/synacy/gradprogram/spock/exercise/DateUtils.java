package com.synacy.gradprogram.spock.exercise;

import java.util.Date;
import java.util.Calendar;

public class DateUtils {

  public Date getCurrentDate() {
    return new Date();
  }

  public static Date getDaysBeforeDate(int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE,days);
    return calendar.getTime();
  }

  public static boolean isWithinThreeDays(Date dateToCheck) {
    Calendar threeDaysAgo = Calendar.getInstance();
    threeDaysAgo.add(Calendar.DATE, -3);

    Calendar calendarToCheck = Calendar.getInstance();
    calendarToCheck.setTime(dateToCheck);

    return threeDaysAgo.get(Calendar.YEAR) == calendarToCheck.get(Calendar.YEAR)
            && threeDaysAgo.get(Calendar.DAY_OF_YEAR) == calendarToCheck.get(Calendar.DAY_OF_YEAR);
  }
}
