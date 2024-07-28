package com.synacy.gradprogram.spock.exercise

import spock.lang.Specification

class RefundServiceSpec extends Specification {

    RefundService refundService

    void setup() {
        refundService = new RefundService()
    }

    def "createAndSaveRefundRequest should create a RefundRequest with #refundRequestStatus of TO_PROCESS and save it"() {

    }
}
