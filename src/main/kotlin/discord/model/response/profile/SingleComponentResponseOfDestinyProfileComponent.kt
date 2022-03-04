package discord.model.response.profile

data class SingleComponentResponseOfDestinyProfileComponent(
    val data: DestinyProfileComponent,
    val privacy: Int,
    val disabled: Boolean
)
