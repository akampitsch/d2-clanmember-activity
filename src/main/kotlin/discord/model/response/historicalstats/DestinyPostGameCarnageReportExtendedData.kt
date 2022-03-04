package discord.model.response.historicalstats

data class DestinyPostGameCarnageReportExtendedData(
    val weapons: DestinyHistoricalWeaponStats,
    val values: DestinyHistoricalStatsValue
)
