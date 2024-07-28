package com.synacy.gradprogram.spock.exercise;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

  public Date getCurrentDate() {
    return new Date();
  }

  public int calculateDifferenceInDays(Date startDate, Date endDate) {
    return Math.toIntExact(ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant()));
  }
}
