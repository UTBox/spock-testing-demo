package com.synacy.gradprogram.spock.exercise;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

  public Date getCurrentDate() {
    return new Date();
  }

  public static boolean isMoreThanThreeDaysAgo(Date date) {
    Calendar threeDaysAgo = Calendar.getInstance();
    threeDaysAgo.add(Calendar.DAY_OF_YEAR, 3);

    Calendar targetDate = Calendar.getInstance();
    targetDate.setTime(date);

    return targetDate.before(threeDaysAgo);
  }

}
