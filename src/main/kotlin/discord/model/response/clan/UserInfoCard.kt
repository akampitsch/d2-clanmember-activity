package discord.model.response.clan

data class UserInfoCard(
    val supplementalDisplayName: String?,
    val iconPath: String?,
    val crossSaveOverride: Int,
    val isPublic: Boolean,
    val membershipType: Int,
    val membershipId: Long,
    val displayName: String,
    val bungieGlobalDisplayName: String?,
    val bungieGlobalDisplayNameCode: Int?
)
