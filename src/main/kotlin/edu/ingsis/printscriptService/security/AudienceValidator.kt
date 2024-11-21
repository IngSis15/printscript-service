package edu.ingsis.printscriptService.security

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import java.lang.System.getLogger

class AudienceValidator(private val audience: String) : OAuth2TokenValidator<Jwt> {
    private val logger = getLogger(AudienceValidator::class.simpleName)

    override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
        val error = OAuth2Error("invalid_token", "The required audience is missing", null)

        if (!jwt.audience.contains(audience)) {
            logger.log(System.Logger.Level.ERROR, "Audience validation failed for token: ${jwt.tokenValue}")
        }

        return if (jwt.audience.contains(audience)) {
            OAuth2TokenValidatorResult.success()
        } else {
            OAuth2TokenValidatorResult.failure(error)
        }
    }
}
