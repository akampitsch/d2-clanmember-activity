package discord.model.response.carnagereport

import discord.model.response.historicalstats.DestinyHistoricalStatsValue

data class DestinyPostGameCarnageReportTeamEntry (
    val teamId: Int,
    val standing: DestinyHistoricalStatsValue,
    val score: DestinyHistoricalStatsValue,
    val teamName: String
)
