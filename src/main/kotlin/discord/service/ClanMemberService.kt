package discord.service

import discord.component.AppCoroutineScope
import discord.component.RestClient
import discord.configuration.ApplicationProperties
import discord.listener.IListener
import discord.model.dao.ActivityCounter
import discord.model.dao.ClanMember
import discord.model.response.clan.GetMembersOfGroup
import discord.repository.ClanMemberRepository
import discord.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import java.util.concurrent.atomic.AtomicInteger

@Service
class ClanMemberService @Autowired constructor(
    private val scope: AppCoroutineScope,
    val restClient: RestClient,
    val characterService: CharacterService,
    val clanMemberRepository: ClanMemberRepository,
    private val applicationProperties: ApplicationProperties
) : Logging, IListener {
    private val baseUrl = applicationProperties.baseUrl
    private val clanMemberEndpoint = "GroupV2/${applicationProperties.groupId}/Members/"
    private lateinit var listener: IListener
    
    @Throws(Exception::class)
    suspend fun getAndStoreMembers() {
        val clanMembers = getMembers()
        val responseMembers = mutableListOf<ClanMember>()
        val jobs = mutableListOf<Job>()
        val counter = AtomicInteger()
        val startTime = System.currentTimeMillis()

        try {
            val clanResponse =
                restClient.executeRequest("$baseUrl$clanMemberEndpoint", HttpMethod.GET, Util.createHttpEntity(applicationProperties.apiKey), GetMembersOfGroup::class.java)

            for (responseEntry in clanResponse.body!!.response.results) {
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
            if (clanMembers.isEmpty()) {

                responseMembers.forEach {
                    jobs.add(
                        scope.launch {
                            it.characters = characterService.getAndStoreCharacters(it)
                            it.activityCounter.clanMember = it
                            clanMemberRepository.save(it)
                            counter.getAndIncrement()
                        })
                }

            } else {
                responseMembers.forEach {
                    if (!clanMembers.contains(it)) {
                        logger.warn("Could not find $it in DB")
                        jobs.add(
                            scope.launch {
                                it.characters = characterService.getAndStoreCharacters(it)
                                it.activityCounter.clanMember = it
                                clanMemberRepository.save(it)
                                counter.getAndIncrement()
                            })
                    }
                }
            }
        } catch (e: ResourceAccessException) {
            e.message?.let { logger.error(it, e) }
            throw e
        } catch (e1: DataIntegrityViolationException) {
            e1.message?.let { logger.warn(it, e1) }
        }

        for (job in jobs)
            job.join()

        val endTime = System.currentTimeMillis()

        logger.info("Scraped ${counter.get()} Members and there Characters in ${Util.convertMillisToTimeStamp(endTime - startTime)}")

    }

    fun getMembers(): List<ClanMember> {
        return clanMemberRepository.findAll()
    }

    @Throws(Exception::class)
    fun updateMember(clanMember: ClanMember) {
        try {
            clanMemberRepository.save(clanMember)
        } catch (e1: DataIntegrityViolationException) {
            e1.message?.let { logger.warn(it, e1) }
        }
    }

    override fun setListener(listener: IListener) {
        this.listener = listener
    }

    override fun notify(string: String) {
        listener.notify(string)
    }
}