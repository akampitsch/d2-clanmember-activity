package discord.model.response.clan

import com.fasterxml.jackson.annotation.JsonProperty

data class GetMembersOfGroup(
    @JsonProperty("ErrorCode") val errorCode: Int,
    @JsonProperty("ThrottleSeconds") val throttleSeconds: Int,
    @JsonProperty("ErrorStatus") val errorStatus: String,
    @JsonProperty("Message") val message: String,
    @JsonProperty("MessageData") val messageData: MessageData,
    @JsonProperty("Response") val response: SearchResultOfGroupMember
)
