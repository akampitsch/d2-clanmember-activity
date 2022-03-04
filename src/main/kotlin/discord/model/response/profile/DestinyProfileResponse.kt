package discord.model.response.profile

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DestinyProfileResponse (
    val profile: SingleComponentResponseOfDestinyProfileComponent
    
)
