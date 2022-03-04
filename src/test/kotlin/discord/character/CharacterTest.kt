package discord.character

import discord.model.dao.ActivityCounter
import discord.model.dao.ClanMember
import discord.model.response.clan.GetMembersOfGroup
import discord.model.response.profile.GetProfile
import discord.service.CharacterService
import kotlinx.coroutines.*
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
class CharacterTest @Autowired constructor(private val scope: CoroutineScope, private val characterService: CharacterService) : Logging {
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
    fun getOneCharacter() {
        val componentQueryString = "?components=Profiles"
        val profileEndpoint = "Destiny2/3/Profile/4611686018493410729/$componentQueryString"

        val response = restTemplate.exchange("$baseUrl$profileEndpoint", HttpMethod.GET, entity, GetProfile::class.java)

        logger.info(response.body!!.response.profile.data.characterIds)
    }

    @Test
    fun getCharacters() {
        val response = restTemplate.exchange("$baseUrl$clanMemberEndpoint", HttpMethod.GET, entity, GetMembersOfGroup::class.java)

        logger.info(response.body!!.response.results.size)
        val responseMembers = mutableListOf<ClanMember>()
        for (responseEntry in response.body!!.response.results) {
            responseMembers.add(
                ClanMember(
                    membershipId = responseEntry.destinyUserInfo.membershipId,
                    displayName = responseEntry.destinyUserInfo.displayName,
                    bungieDisplayName = responseEntry.destinyUserInfo.bungieGlobalDisplayName,
                    memberShipType = responseEntry.destinyUserInfo.membershipType,
                    activityCounter = ActivityCounter(activitySum = 0)
                )
            )
        }

        logger.info(responseMembers)

        runBlocking { coroutineExecution(responseMembers) }
    }

    private suspend fun coroutineExecution(responseMembers: MutableList<ClanMember>) {
        try {
            responseMembers.forEach { scope.launch { it.characters = characterService.getAndStoreCharacters(it) }.join() }
        } catch (e: Exception) {
            e.message?.let { logger.error(it, e) }
        }
    }
}