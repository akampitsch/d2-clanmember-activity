package discord.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ApplicationProperties {
    @Value("\${bungie.api.key}")
    lateinit var apiKey: String

    @Value("\${bungie.api.baseUrl}")
    lateinit var baseUrl: String

    @Value("\${bungie.clan.groupId}")
    lateinit var groupId: String

    @Value("\${bungie.api.profile.components}")
    lateinit var components: String

    @Value("\${bungie.api.activity.rowCount}")
    lateinit var rowCount: String
}