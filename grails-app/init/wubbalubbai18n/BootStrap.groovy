package wubbalubbai18n

import grails.core.GrailsApplication

class BootStrap {
    GrailsApplication grailsApplication
    SelfAssessmentRatingService selfAssessmentRatingService

    def init = { servletContext ->
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))


        selfAssessmentRatingService.createRatingsFromConfig()
    }
    def destroy = {
    }
}
