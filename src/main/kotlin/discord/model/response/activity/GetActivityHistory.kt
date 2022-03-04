package discord.model.response.activity

import com.fasterxml.jackson.annotation.JsonProperty
import discord.model.response.clan.MessageData

data class GetActivityHistory(
    @JsonProperty("ErrorCode") var errorCode: Int,
    @JsonProperty("ThrottleSeconds") var throttleSeconds: Int,
    @JsonProperty("ErrorStatus") var errorStatus: String,
    @JsonProperty("Message") var message: String,
    @JsonProperty("MessageData") var messageData: MessageData,
    @JsonProperty("Response") val response: DestinyActivityHistoryResults

)
