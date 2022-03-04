package discord.model.response.profile

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import discord.model.response.clan.UserInfoCard

@JsonIgnoreProperties(ignoreUnknown = true)
data class DestinyProfileComponent (
    val userInfoCard: UserInfoCard?,
    val characterIds: List<Long>,
)
