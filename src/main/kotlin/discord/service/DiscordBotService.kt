package discord.service

import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Message
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import discord.component.AppCoroutineScope
import discord.listener.IListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
final class DiscordBotService @Autowired constructor(
    scope: AppCoroutineScope,
    private val collationService: CollationService,
    private val memberService: ClanMemberService,
    private val activityService: ActivityService
) :
    Logging, IListener {

    private val commands = listOf("blah", "members", "activities", "collate")
    private var working = false
    private lateinit var lastMessage: Message

    init {
        scope.launch { startBot() }
        collationService.setListener(this)
        memberService.setListener(this)
        activityService.setListener(this)
    }

    private suspend fun startBot() {
        val client = Kord("OTQ5MzM4OTg3MjMzOTQzNjAy.YiI61A.N89iBRGZr6TbYLJPR6H1o-sU4Gk")
        client.on<MessageCreateEvent> {
            logger.trace(message)
            
            if (working) return@on
            
            when (message.content) {
                "!pong" -> doPong(message)
                "/members" -> scrapeMembers(message)
                "/activities" -> scrapeActivities(message)
                "/collate" -> doCollation(message)
                else -> return@on
            }

        }

        client.login()

    }

    private suspend fun doCollation(message: Message) {
        TODO("Not yet implemented")
    }

    private suspend fun scrapeActivities(message: Message) {
        TODO("Not yet implemented")
    }

    private suspend fun scrapeMembers(message: Message) {
        val response = message.channel.createMessage("Scraping members now!")
        message.delete()
        lastMessage = response
        working = true
        memberService.getAndStoreMembers()
    }

    private suspend fun doPong(message: Message) {
        val response = message.channel.createMessage("Pong!")
        response.addReaction(ReactionEmoji.Unicode("\uD83C\uDFD3"))

        delay(5000)
        message.delete()
        response.delete()
    }

    override fun setListener(listener: IListener) {
        //nothing
    }

    override fun notify(string: String) {
        logger.debug(string)
    }


}