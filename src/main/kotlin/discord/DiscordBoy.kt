package discord

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DiscordBoy: Logging {

//    private val activities = mutableMapOf<Long, Activity>()
//    private val result = mutableMapOf<String, GroupedOverview>()
//    private val logger: Logger = LogManager.getLogger()
//    private val memberStore = mutableListOf<ClanMember>()
//
//    //    val statsUrl = "https://stats.bungie.net/platform/"
//
//    private final var rowCount = 50
//    private final var startDateTime: LocalDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0)
//    private final var endDateTime: LocalDateTime = LocalDateTime.of(2022, 2, 1, 0, 0, 0, 0)
//
//
//    private final val apiResult = getApiData(rowCount, startDateTime, endDateTime)
//
//    val memberStoreResult = runMemberStore(apiResult)
//
//    val activityStoreResult = runActivityStore()
//
//    private fun runActivityStore(): Long {
//        val timeInMillisActivitiesMap = measureTimeMillis {
//            val activitiesResult = mutableMapOf<String, ActivityCounter>()
//            var activitySum = 0
//            for ((_, activity) in activities) {
//                if (activity.players.size > 1) {
//                    logger.debug("Activity has more than one player: $activity")
//                    activitySum += 1
//                    for (player in activity.players) {
//                        for (mode in activity.modes) {
//                            if (activitiesResult[player] != null) {
//                                if (activitiesResult[player]!!.activityByMode[DestinyActivityModeType.fromInt(mode)] != null) {
//                                    val currentValueMode = activitiesResult[player]!!.activityByMode[DestinyActivityModeType.fromInt(mode)]!!
//                                    val currentValueCount = activitiesResult[player]!!.activitySum
//                                    activitiesResult[player]!!.activityByMode[DestinyActivityModeType.fromInt(mode)] = currentValueMode + 1
//                                    activitiesResult[player]!!.activitySum += 1
//                                } else {
//                                    activitiesResult[player]!!.activityByMode[DestinyActivityModeType.fromInt(mode)] = 1
//                                    activitiesResult[player]!!.activitySum += 1
//                                }
//                            } else {
//                                activitiesResult[player] = ActivityCounter(1, mutableMapOf(DestinyActivityModeType.fromInt(mode) to 1))
//                            }
//                        }
//                    }
//                }
//            }
//            for (entry in activitiesResult) {
//                logger.debug("${entry.key} - ${entry.value}")
//            }
//            
//            logger.info("Total Activities scanned: $activitySum")
//        }
//        logger.info("ActivitiesMap run in ${convertMillisToTimeStamp(timeInMillisActivitiesMap)}")
//        return timeInMillisActivitiesMap
//    }
//
//
//    private fun runMemberStore(timeInMillisApi: Long): Long {
//        for (clanMember in memberStore) {
//            logger.debug("Found ${clanMember.activities.size} for ${clanMember.displayName}")
//        }
//
//        val timeInMillisInternal = measureTimeMillis {
//            for (clanMember in memberStore) {
//                for (partner in memberStore) {
//                    for (memberActivity in clanMember.activities) {
//                        if (!memberActivity.date.isBefore(LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0))
//                            && !memberActivity.date.isAfter(LocalDateTime.of(2022, 2, 1, 0, 0, 0, 0))
//                        ) {
//                            for (partnerActivity in partner.activities) {
//                                val temp = PlayedWith(memberActivity.instanceId, memberActivity.date, memberActivity.modes, partner.displayName)
//                                if (memberActivity.instanceId == partnerActivity.instanceId && !clanMember.played.contains(temp) && clanMember != partner) {
//                                    clanMember.played.add(temp)
//                                    logger.debug("Added $temp to ${clanMember.displayName} - partner was ${partner.displayName}")
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            for (clanMember in memberStore) {
//                if (clanMember.played.size > 0) {
//                    logger.info("${clanMember.displayName} played ${clanMember.played.size} with other members")
//                    logger.trace("${clanMember.displayName}: ${clanMember.played}")
//                    for (entry in clanMember.played)
//                        if (result[clanMember.displayName] != null) {
//                            for (helper in entry.mode)
//                                if (result[clanMember.displayName]!!.played[DestinyActivityModeType.fromInt(helper)] != null) {
//                                    val currentValue = result[clanMember.displayName]!!.played[DestinyActivityModeType.fromInt(helper)]!!
//                                    result[clanMember.displayName]!!.played[DestinyActivityModeType.fromInt(helper)] = currentValue + 1
//                                } else {
//                                    result[clanMember.displayName]!!.played[DestinyActivityModeType.fromInt(helper)] = 1
//                                }
//                        } else {
//                            for (helper in entry.mode) {
//                                result[clanMember.displayName] = GroupedOverview(mutableMapOf(DestinyActivityModeType.fromInt(helper) to 1))
//                            }
//                        }
//                }
//            }
//        }
//
//        logger.info("Sifting trough activities done in ${convertMillisToTimeStamp(timeInMillisInternal)}")
//        logger.info("Finished entire run in ${convertMillisToTimeStamp(timeInMillisApi + timeInMillisInternal)}")
//        return timeInMillisInternal
//    }
//
//    private fun getApiData(rowCount: Int, startDateTime: LocalDateTime, endDateTime: LocalDateTime): Long {
//        
//
//
//
//
//            for (clanMember in memberStore)
//                for (characterId in clanMember.characters) {
//                    
//        }
//
//        logger.info("Gathering Data from Bungie API finished in ${convertMillisToTimeStamp(timeInMillisApi)}")
//
//        return timeInMillisApi
//    }
//
//
//
//    fun addActivitiesToActivityMap(
//        startDateTime: LocalDateTime,
//        endDateTime: LocalDateTime,
//        activities: MutableMap<Long, Activity>,
//        member: ClanMember,
//        activitiesList: List<DestinyHistoricalStatsPeriodGroup>
//    ): Long {
//
//        var tracker = 0L
//        val logger = LogManager.getLogger()
//        for (activity in activitiesList) {
//            if (!activity.period.isBefore(startDateTime) && !activity.period.isAfter(endDateTime)) {
//                if (activities[activity.activityDetails.instanceId] != null) {
//                    logger.debug("found existing instanceId ${activity.activityDetails.instanceId} - ${activities[activity.activityDetails.instanceId]}")
//                    if (!activities[activity.activityDetails.instanceId]!!.players.contains(member.displayName)) {
//                        logger.debug("Adding ${member.displayName} to ${activities[activity.activityDetails.instanceId]!!.players}")
//                        activities[activity.activityDetails.instanceId]!!.players += member.displayName
//                        tracker += 1
//                        logger.debug("List after addition: ${activities[activity.activityDetails.instanceId]!!.players}")
//                    } else {
//                        logger.debug("${member.displayName} already in ${activities[activity.activityDetails.instanceId]!!.players}")
//                    }
//                } else {
//                    val toAdd = Activity(activity.activityDetails.instanceId, activity.period, activity.activityDetails.modes, mutableListOf(member.displayName))
//                    logger.debug("Creating ${activity.activityDetails.instanceId} entry with $toAdd")
//                    activities[activity.activityDetails.instanceId] = toAdd
//                    tracker += 1
//                }
//            } else {
//                logger.warn("Activity Date outside specified Timeframe: $activity")
//            }
//        }
//        return tracker
//    }
//
//    fun addActivitiesToClanMember(member: ClanMember, characterId: Long, activities: List<DestinyHistoricalStatsPeriodGroup>) {
//        for (activity in activities)
//            member.activities.add(ClanMemberActivity(activity.activityDetails.instanceId, activity.period, characterId, activity.activityDetails.modes))
//    }
}

fun main(args: Array<String>) {
    runApplication<DiscordBoy>(*args)
}