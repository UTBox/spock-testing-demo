package com.synacy.gradprogram.spock.exercise;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

  public Date getCurrentDate() {
    return new Date();
  }

  public static boolean isMoreThanThreeDaysAgo(Date date) {
    Calendar threeDaysAgo = Calendar.getInstance();
    threeDaysAgo.add(Calendar.DAY_OF_MONTH, 3);

    Calendar targetDate = Calendar.getInstance();
    targetDate.setTime(date);

    return targetDate.before(threeDaysAgo);

  }

  public static Date subtractDays(Date date, int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, days);
    return calendar.getTime();
  }

}
