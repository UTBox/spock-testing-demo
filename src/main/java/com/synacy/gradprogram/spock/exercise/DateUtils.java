package com.synacy.gradprogram.spock.exercise;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

  public Date getCurrentDate() {
    return new Date();
  }

  public int getDateDiffInDays(Date olderDate, Date newerDate) {
    return (int) ChronoUnit.DAYS.between(olderDate.toInstant(), newerDate.toInstant());
  }



}
