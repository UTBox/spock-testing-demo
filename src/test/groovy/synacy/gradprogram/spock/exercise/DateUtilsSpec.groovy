package synacy.gradprogram.spock.exercise

import com.synacy.gradprogram.spock.exercise.DateUtils
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

class DateUtilsSpec extends Specification {

    DateUtils dateUtils

    void setup() {
        dateUtils = new DateUtils()
    }

    def "calculateDifferenceInDays should return 0 for the same dates"() {
        given:
        Date startDate = new Date()
        Date endDate = startDate

        expect:
        this.dateUtils.calculateDifferenceInDays(startDate, endDate) == 0
    }

    def "calculateDifferenceInDays should return correct number of days for different dates"() {
        given:
        Date startDate = Date.from(LocalDate.of(2023, 7, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        Date endDate = Date.from(LocalDate.of(2023, 7, 5).atStartOfDay(ZoneId.systemDefault()).toInstant())

        expect:
        this.dateUtils.calculateDifferenceInDays(startDate, endDate) == 4
    }
}
