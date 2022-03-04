package discord.model.response.historicalstats

data class DestinyHistoricalStatsActivity (
    val referenceId: Long,
    val directorActivityHash: Long,
    val instanceId: Long,
    val mode: Int,
    val modes: List<Int>,
    val isPrivate: Boolean,
    val memberShipType: Int
)
