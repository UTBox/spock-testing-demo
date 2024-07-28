package com.synacy.gradprogram.spock.exercise;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

  public Date getCurrentDate() {
    return new Date();
  }

  public boolean isDateWithinRefundPeriod(Date givenDate) {
    final long DAYS_WITHIN_REFUND_PERIOD = 3;

    LocalDate orderDate = givenDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate cancellationDate = LocalDate.now();
    LocalDate refundDateWindow = cancellationDate.minusDays(DAYS_WITHIN_REFUND_PERIOD);

    return !orderDate.isBefore(refundDateWindow) && !orderDate.isAfter(cancellationDate);
  }

}
