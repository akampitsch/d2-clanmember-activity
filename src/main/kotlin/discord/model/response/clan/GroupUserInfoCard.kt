package discord.model.response.clan

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupUserInfoCard(
    @JsonProperty("LastSeenDisplayName") val lastSeenDisplayName: String,
    @JsonProperty("LastSeenDisplayNameType") val lastSeenDisplayNameType: Int,
    val iconPath: String,
    val crossSaveOverride: Int,
    val applicableMembershipTypes: List<Int>,
    val isPublic: Boolean,
    val membershipType: Int,
    val membershipId: Long,
    val displayName: String,
    val bungieGlobalDisplayName: String,
    val bungieGlobalDisplayNameCode: Int?
)
