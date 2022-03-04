package discord.service

import discord.component.RestClient
import discord.configuration.ApplicationProperties
import discord.model.dao.Character
import discord.model.dao.ClanMember
import discord.model.response.profile.GetProfile
import discord.repository.CharacterRepository
import discord.util.Util
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException

@Service
class CharacterService @Autowired constructor(
    private val characterRepository: CharacterRepository,
    private val restClient: RestClient,
    private val applicationProperties: ApplicationProperties
) : Logging {
    private val baseUrl = applicationProperties.baseUrl
    private val componentQueryString = "?components=${applicationProperties.components}"

    @Throws(Exception::class)
    suspend fun getAndStoreCharacters(member: ClanMember): MutableList<Character> {
        val profileEndpoint = "Destiny2/${member.memberShipType}/Profile/${member.membershipId}/$componentQueryString"
        val characters: MutableList<Character> = mutableListOf()

        try {
            logger.debug("Getting Characters for $member from ${Thread.currentThread().name}")
            val response = restClient.executeRequest(
                "$baseUrl$profileEndpoint",
                HttpMethod.GET,
                Util.createHttpEntity(applicationProperties.apiKey),
                GetProfile::class.java
            )

            for (characterId in response.body!!.response.profile.data.characterIds) {
                val character = characterRepository.save(Character(characterId = characterId))
                characters.add(character)
            }
        } catch (e: ResourceAccessException) {
            e.message?.let { logger.error(it, e) }
            throw e
        } catch (e1: DataIntegrityViolationException) {
            e1.message?.let { logger.warn(it, e1) }
        }

        return characters
    }

}