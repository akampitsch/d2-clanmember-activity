package discord.model.response.carnagereport

import discord.model.response.historicalstats.DestinyHistoricalStatsActivity
import java.time.LocalDateTime

data class DestinyPostGameCarnageReportData (
    val period: LocalDateTime,
    val startingPhaseIndex: Int?,
    val activityDetails: DestinyHistoricalStatsActivity,
    val entries: List<DestinyPostGameCarnageReportEntry>,
    val teams: List<DestinyPostGameCarnageReportTeamEntry>
)
