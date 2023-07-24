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
}
