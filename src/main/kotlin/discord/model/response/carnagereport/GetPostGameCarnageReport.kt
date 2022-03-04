@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused"
)

package discord.model.response.carnagereport

import com.fasterxml.jackson.annotation.JsonProperty
import discord.model.response.clan.MessageData

data class GetPostGameCarnageReport(
    @JsonProperty("ErrorCode") val errorCode: Int,
    @JsonProperty("ThrottleSeconds") val throttleSeconds: Int,
    @JsonProperty("ErrorStatus") val errorStatus: String,
    @JsonProperty("Message") val message: String,
    @JsonProperty("MessageData") val messageData: MessageData,
    @JsonProperty("Response") val response: DestinyPostGameCarnageReportData
)
