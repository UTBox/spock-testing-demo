package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

import java.text.SimpleDateFormat

class DateUtilsSpec extends Specification {
    DateUtils dateUtils

    def setup(){
        dateUtils = new DateUtils()
    }

    def "getDateDiffInDays should return the number of days between two dates"(){
        when:
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date olderDate = formatter.parse(olderDateRaw)
        Date newerDate = formatter.parse(newerDateRaw)

        int actualDiff = dateUtils.getDateDiffInDays(olderDate, newerDate)

        then:
        dateDiff == actualDiff

        where:
        olderDateRaw  | newerDateRaw   | dateDiff
        "2024-1-1"    | "2024-1-15"    | 14
        "2024-0-1"    | "2024-2-1"     | 62
        "2024-1-1"    | "2025-1-1"     | 366
    }
}
