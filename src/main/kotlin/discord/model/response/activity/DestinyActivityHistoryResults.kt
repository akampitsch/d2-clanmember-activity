package discord.model.response.activity

import discord.model.response.historicalstats.DestinyHistoricalStatsPeriodGroup

data class DestinyActivityHistoryResults (
val activities: List<DestinyHistoricalStatsPeriodGroup>?
)
