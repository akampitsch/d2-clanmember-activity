package discord.service

import discord.component.AppCoroutineScope
import discord.component.RestClient
import discord.configuration.ApplicationProperties
import discord.model.dao.Activity
import discord.model.dao.ClanMember
import discord.model.response.activity.GetActivityHistory
import discord.model.response.enum.DestinyActivityModeType
import discord.model.response.historicalstats.DestinyHistoricalStatsPeriodGroup
import discord.repository.ActivityRepository
import discord.util.Util
import kotlinx.coroutines.*
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import kotlin.jvm.Throws

@Service
class ActivityService @Autowired constructor(
    private val scope: AppCoroutineScope,
    val activityRepository: ActivityRepository,
    val restClient: RestClient,
    private val applicationProperties: ApplicationProperties
) : Logging {
    private val baseUrl = applicationProperties.baseUrl
    private val rowCount = applicationProperties.rowCount

    @Throws(Exception::class)
    suspend fun getAndStoreActivities(clanMembers: List<ClanMember>, startDateTime: LocalDateTime) {
        val result = mutableMapOf<ClanMember, MutableList<DestinyHistoricalStatsPeriodGroup>>()
        val jobs = mutableListOf<Job>()
        val counter = AtomicInteger()
        val startTime = System.currentTimeMillis()

        for (member in clanMembers) {
            jobs.add(scope.launch { result.putAll(getActivitiesByMember(member, startDateTime)) })
        }

        for (job in jobs)
            job.join()

        for ((member, activities) in result) {
            for (activity in activities) {
                createOrUpdateActivity(member, activity)
                counter.getAndIncrement()
            }
        }

        val endTime = System.currentTimeMillis()

        logger.info("Scraped $counter Activities in ${Util.convertMillisToTimeStamp(endTime - startTime)}")
    }

    @Throws(Exception::class)
    fun getActivitiesByMember(clanMember: ClanMember, startDateTime: LocalDateTime): MutableMap<ClanMember, MutableList<DestinyHistoricalStatsPeriodGroup>> {
        logger.info(Thread.currentThread().name)
        val activities = mutableMapOf<ClanMember, MutableList<DestinyHistoricalStatsPeriodGroup>>()

        for (character in clanMember.characters) {
            var currentPage = 1
            val activitiesEndpoint = "/Destiny2/${clanMember.memberShipType}/Account/${clanMember.membershipId}/Character/${character.characterId}/Stats/Activities/"

            var nextPage = true
            try {
                while (nextPage) {
                    logger.info("Getting Page $currentPage for ${clanMember.displayName}'s characterId: ${character.characterId}")
                    val response =
                        restClient.executeRequest(
                            "$baseUrl$activitiesEndpoint?count=$rowCount&page=$currentPage",
                            HttpMethod.GET,
                            Util.createHttpEntity(applicationProperties.apiKey),
                            GetActivityHistory::class.java
                        )
                    if (response.body!!.response.activities != null) {
                        var responseActivities = response.body!!.response.activities!!
                        val sortedList = response.body!!.response.activities!!.sortedByDescending { it.period }
                        logger.trace("sortedList: first period ${sortedList.first()}- last period ${sortedList.last()}")
                        logger.trace("unsortedList: first period ${response.body!!.response.activities!!.first()} - last period ${response.body!!.response.activities!!.last()}")

                        if (sortedList == response.body!!.response.activities) {
                            logger.trace("Lists should be sorted the same")
                        } else {
                            responseActivities = sortedList
                        }

                        for (element in responseActivities) {
                            if (activities[clanMember] == null) {
                                activities[clanMember] = mutableListOf(element)
                            } else {
                                activities[clanMember]!!.add(element)
                            }
                        }

                        if (responseActivities.last().period.isBefore(startDateTime)) {
                            nextPage = false
                        } else {
                            logger.debug("${responseActivities.last().period} after $startDateTime grabbing an additional Page")
                            currentPage += 1
                        }
                    } else {
                        logger.debug("Activity lookup returned no activities for ${clanMember.displayName}'s characterId ${character.characterId}")
                        nextPage = false
                    }
                }

            } catch (e: ResourceAccessException) {
                e.message?.let { logger.error(it, e) }
                throw e
            }
        }

        return activities
    }

    @Throws(Exception::class)
    fun createOrUpdateActivity(clanMember: ClanMember, responseActivity: DestinyHistoricalStatsPeriodGroup): Activity {
        var activity = activityRepository.findByInstanceId(responseActivity.activityDetails.instanceId)

        if (activity == null) {
            activity = Activity(instanceId = responseActivity.activityDetails.instanceId, date = responseActivity.period, modes = mutableListOf(), players = mutableListOf())
            activity.players.add(clanMember)
            for (mode in responseActivity.activityDetails.modes) {
                try
                {
                    activity.modes.add(DestinyActivityModeType.fromInt(mode))
                } catch (e: Exception)
                {
                    logger.trace(e)
                    e.message?.let { logger.error(it, e) }
                }
            }
        } else {
            if (!activity.players.contains(clanMember))
                activity.players.add(clanMember)
        }

        try {
            activityRepository.save(activity)
        } catch (e1: DataIntegrityViolationException) {
            e1.message?.let { logger.warn(it, e1) }
        }


        return activity
    }

    fun getActivities(): List<Activity> {
        return activityRepository.findAll()
    }
}

/**
 *                             addActivitiesToClanMember(clanMember, characterId, helper)
if (tracker["${clanMember.displayName} - $characterId"] != null) {
val currentValue = tracker["${clanMember.displayName} - $characterId"]
val toAdd = addActivitiesToActivityMap(startDateTime, endDateTime, activities, clanMember, helper)
tracker["${clanMember.displayName} - $characterId"] = currentValue!! + toAdd
} else {
tracker["${clanMember.displayName} - $characterId"] = addActivitiesToActivityMap(startDateTime, endDateTime, activities, clanMember, helper)
}
 */