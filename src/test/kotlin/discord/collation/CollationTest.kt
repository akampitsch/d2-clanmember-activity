package discord.collation

import discord.model.dao.Activity
import discord.model.dao.ActivityCounter
import discord.model.dao.ClanMember
import discord.model.response.enum.DestinyActivityModeType
import discord.repository.ActivityRepository
import discord.repository.ClanMemberRepository
import discord.service.CollationService
import org.apache.logging.log4j.kotlin.Logging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import kotlin.random.Random


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CollationTest @Autowired constructor(
    private val activityRepository: ActivityRepository,
    private val memberRepository: ClanMemberRepository,
    private val collationService: CollationService,
    private val restTemplate: TestRestTemplate
    ) : Logging {
    private var members = mutableListOf<ClanMember>()
    private val startDateTime: LocalDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0)
    private val endDateTime: LocalDateTime = LocalDateTime.of(2022, 2, 1, 0, 0, 0, 0)

    @BeforeAll
    fun setup() {
        for (i in 1..15) {
            members.add(
                ClanMember(
                    membershipId = i.toLong(),
                    displayName = "member$i",
                    bungieDisplayName = "member$i",
                    memberShipType = 3,
                    activityCounter = ActivityCounter(activitySum = 0)
                )
            )
        }

        memberRepository.saveAll(members)
        members = memberRepository.findAll()

        try {
            for (i in 1..1000) {
                val players: MutableList<ClanMember> = MutableList(Random.nextInt(1, 6)) { members[Random.nextInt(0, 14)] }
                val modes: MutableList<DestinyActivityModeType> = try {
                    MutableList(Random.nextInt(1, 3)) { DestinyActivityModeType.fromInt(Random.nextInt(0, 82)) }
                } catch (e: NoSuchElementException) {
                    mutableListOf(DestinyActivityModeType.None)
                }

                val activity =
                    Activity(
                        instanceId = Random.nextLong(999999999L),
                        players = players,
                        date = LocalDateTime.of(2022, 1, Random.nextInt(1, 31), 0, 0, 0),
                        modes = modes.map { it } as MutableList<DestinyActivityModeType>
                    )

                activityRepository.save(activity)
            }
        } catch (e: Exception) {
            e.message?.let { logger.error(it, e) }
        }
    }

    @Test
    suspend fun runCollation() {
        collationService.runCollation(activityRepository.findAll(), startDateTime, endDateTime)
        
        for (member in memberRepository.findAll())
        {
            logger.info("${member.displayName}: ${member.activityCounter}")
        }
    }
    
    @AfterAll
    fun getResult() {
        val entity = restTemplate.getForEntity<String>("/")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        entity.body?.let { logger.info(it) }
    }
}