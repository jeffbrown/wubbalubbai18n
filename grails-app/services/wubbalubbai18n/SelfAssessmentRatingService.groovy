package wubbalubbai18n

import grails.compiler.GrailsCompileStatic
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import org.springframework.context.MessageSource

@GrailsCompileStatic
@Slf4j
@Transactional
class SelfAssessmentRatingService implements GrailsConfigurationAware {

    MessageSource messageSource

    List<Map> setupAssessmentRatings

    @Override
    void setConfiguration(Config co) {
        List selfAssessmentRatings = co.getProperty('setup.selfAssessmentRatings', List, null)
        setupAssessmentRatings = selfAssessmentRatings as List<Map>
    }

    def createRatingsFromConfig() {
        if (setupAssessmentRatings == null || setupAssessmentRatings.empty) {
            log.info("Skipping creating SelfAssessmentRatings because none were specified " +
                    "in the config: 'setup.selfAssessmentRatings'")
            return
        }

        log.info("Saving ${setupAssessmentRatings.size()} new SelfAssessmentRating records")
        for (Map map in setupAssessmentRatings) {
            // SelfAssessmentRating is a domain class
            SelfAssessmentRating newRating = new SelfAssessmentRating()
            newRating.rating = map.rating as Integer
            newRating.englishText = map.englishText
            newRating.translationKey = map.translationKey
            newRating.save(failOnError: true)
            // Verify we have that translation. This will throw an exception if the translation is not present.
            println messageSource.getMessage(newRating.translationKey, [].toArray(), Locale.default)
        }
    }
}
