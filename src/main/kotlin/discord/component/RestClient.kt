package discord.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RestClient @Autowired constructor(
    val retryTemplate: RetryTemplate,
    val restTemplate: RestTemplate
) {
    fun <T : Any, E : Any> executeRequest(
        url: String,
        method: HttpMethod,
        requestEntity: HttpEntity<E>,
        responseType: Class<T>
    ): ResponseEntity<T> {
        return retryTemplate.execute<ResponseEntity<T>, Exception> {
            restTemplate.exchange(url, method, requestEntity, responseType)
        }
    }
}