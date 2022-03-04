package discord.service

import discord.component.AppCoroutineScope
import discord.model.dao.Activity
import discord.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CollationService @Autowired constructor(
    private val scope: AppCoroutineScope,
    val clanMemberService: ClanMemberService,
) : Logging {
    @Throws(Exception::class)
    suspend fun runCollation(activities: List<Activity>, startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        val jobs = mutableListOf<Job>()
        val startTime = System.currentTimeMillis()

        for (activity in activities) {
            if (activity.players.size > 1) {
                for (player in activity.players) {
                    jobs.add(
                        scope.launch {
                            if (activity.date.isBefore(startDateTime) || activity.date.isAfter(endDateTime)) {
                                logger.debug("Activity with instanceId ${activity.instanceId} and dateTime ${activity.date} outside of specified Range")
                            } else {

                                logger.debug("Adding Activity ${activity.instanceId}/${activity.modes} to ${player.displayName}")
                                player.activityCounter.activitySum += 1
                                for (mode in activity.modes) {
                                    val activityCounterPerModeSum = player.activityCounter.activitiesByMode[mode]
                                    if (activityCounterPerModeSum != null) {
                                        player.activityCounter.activitiesByMode[mode] = activityCounterPerModeSum + 1L
                                    } else {
                                        player.activityCounter.activitiesByMode[mode] = 1L
                                    }
                                }
                            }
                        })
                    for (job in jobs)
                        job.join()
                    clanMemberService.updateMember(player)
                }
            }
        }

        val endTime = System.currentTimeMillis()
        logger.info("Finished Collation in ${Util.convertMillisToTimeStamp(endTime - startTime)}")
    }
}
