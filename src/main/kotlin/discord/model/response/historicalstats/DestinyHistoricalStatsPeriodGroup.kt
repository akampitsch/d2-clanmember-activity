package discord.model.response.historicalstats

import java.time.LocalDateTime

data class DestinyHistoricalStatsPeriodGroup (
    val period: LocalDateTime,
    val activityDetails: DestinyHistoricalStatsActivity,
    val values: DestinyHistoricalStatsValue,
    val extended: DestinyPostGameCarnageReportExtendedData?
    )

