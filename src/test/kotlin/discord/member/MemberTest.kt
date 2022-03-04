package discord.member

import discord.model.response.clan.GetMembersOfGroup
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.time.Duration

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberTest @Autowired constructor() : Logging {
    private val groupID = "3351106"
    private val baseUrl = "https://www.bungie.net/platform/"
    private val clanMemberEndpoint = "GroupV2/$groupID/Members/"
    lateinit var restTemplate: RestTemplate
    lateinit var entity: HttpEntity<String>

    @BeforeAll
    internal fun setup() {
        val headers = HttpHeaders()
        headers.set("X-API-Key", "c8db524a85da452c8d0b1c1e0f36deb7")
        entity = HttpEntity("body", headers)

        val restTemplateBuilder = RestTemplateBuilder()
        restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(60)).setReadTimeout(Duration.ofSeconds(60)).build()
    }

    @Test
    fun getMembers() {
        val response = restTemplate.exchange("$baseUrl$clanMemberEndpoint", HttpMethod.GET, entity, GetMembersOfGroup::class.java)

        logger.info(response.body!!.response.results.size)

        for (member in response.body!!.response.results) {
            if (member.bungieNetUserInfo?.displayName == "forSeth") {
                logger.info(member)
            }

            if (member.destinyUserInfo.displayName == "forSeth") {
                logger.info(member)
            }
        }

    }
}
