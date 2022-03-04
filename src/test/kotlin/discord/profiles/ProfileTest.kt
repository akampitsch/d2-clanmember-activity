package discord.profiles

import discord.model.response.profile.GetProfile
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.time.Duration

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileTest : Logging {
    private val baseUrl = "https://www.bungie.net/platform/"
    lateinit var restTemplate: RestTemplate
    lateinit var entity: HttpEntity<String>
    private val componentQueryString = "?components=Profiles"
    private val profileEndpoint = "Destiny2/3/Profile/4611686018493410729/$componentQueryString"

    @BeforeAll
    internal fun setup() {
        val headers = HttpHeaders()
        headers.set("X-API-Key", "c8db524a85da452c8d0b1c1e0f36deb7")
        entity = HttpEntity("body", headers)

        val restTemplateBuilder = RestTemplateBuilder()
        restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(60)).setReadTimeout(Duration.ofSeconds(60)).build()
    }

    @Test
    fun testActivityApi() {
        val response = restTemplate.exchange("$baseUrl$profileEndpoint", HttpMethod.GET, entity, GetProfile::class.java)


        logger.info(response.body!!.response.profile.data.characterIds)

    }
}