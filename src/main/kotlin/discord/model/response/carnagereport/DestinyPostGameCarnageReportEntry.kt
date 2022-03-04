package discord.model.response.carnagereport

import discord.model.response.historicalstats.DestinyHistoricalStatsValue
import discord.model.response.historicalstats.DestinyPlayer

data class DestinyPostGameCarnageReportEntry (
    val standing: Int,
    val score: DestinyHistoricalStatsValue,
    val player: DestinyPlayer,
    val characterId: Long,
    val values: DestinyHistoricalStatsValue
)
