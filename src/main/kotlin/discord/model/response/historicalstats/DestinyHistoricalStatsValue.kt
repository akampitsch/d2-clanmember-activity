package discord.model.response.historicalstats

data class DestinyHistoricalStatsValue(
    val statId: String?,
    val basic: DestinyHistoricalStatsValuePair?,
    val pga: DestinyHistoricalStatsValuePair?,
    val weighted: DestinyHistoricalStatsValuePair?,
    val activityId: Int?
    )
