package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class DateUtilsSpec extends Specification {
    DateUtils dateUtils

    def setup(){
        dateUtils = new DateUtils()
    }

    def "getDateDiffInDays should return the number of days between two dates"(){
        when:
        int actualDiff = dateUtils.getDateDiffInDays(olderDate, newerDate)

        then:
        dateDiff == actualDiff

        where:
        olderDate            | newerDate             | dateDiff
        new Date(2024, 1, 1) | new Date(2024, 1, 15) | 14
        new Date(2024, 0, 1) | new Date(2024, 2, 1)  | 60
        new Date(2024, 1, 1) | new Date(2025, 1, 1)  | 366
    }
}
