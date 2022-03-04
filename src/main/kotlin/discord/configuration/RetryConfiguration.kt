package discord.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate


@Configuration
class RetryConfiguration {
    @Value("\${bungie.api.retry.attempts}")
    private val maxRetryAttempts = 5

    private val simpleRetryPolicy = SimpleRetryPolicy(maxRetryAttempts)

    @Bean
    fun retryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()
        retryTemplate.setRetryPolicy(simpleRetryPolicy)
        return retryTemplate
    }
}