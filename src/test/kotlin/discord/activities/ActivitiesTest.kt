package discord.activities

import discord.model.dao.Activity
import discord.model.response.activity.GetActivityHistory
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.time.LocalDateTime

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivitiesTest: Logging {
    private val characterIds = listOf("2305843009527196770", "2305843009530774512", "2305843009920554564")
    private val baseUrl = "https://www.bungie.net/platform/"
    private val rowCount = "100"
    private val activities = mutableListOf<Activity>()

    lateinit var restTemplate: RestTemplate
    lateinit var entity: HttpEntity<String>

    @BeforeAll
    internal fun setup() {
        val headers = HttpHeaders()
        headers.set("X-API-Key", "c8db524a85da452c8d0b1c1e0f36deb7")
        entity = HttpEntity("body", headers)

        val restTemplateBuilder = RestTemplateBuilder()
        restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(60)).setReadTimeout(Duration.ofSeconds(60)).build()
    }

    @Test
    fun getActivities() {
        for (characterId in characterIds) {
            val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0) 
            val activitiesEndpoint = "/Destiny2/3/Account/4611686018493410729/Character/$characterId/Stats/Activities/"
            var currentPage = 1

            var nextPage = true
            try {
                while (nextPage) {
                    logger.info("################################# $characterId ##################################")
                    logger.debug("Getting Page $currentPage for forSeth's characterId: $characterId")
                    val response =
                        restTemplate.exchange(
                            "$baseUrl$activitiesEndpoint?count=$rowCount&page=$currentPage",
                            HttpMethod.GET,
                            entity,
                            GetActivityHistory::class.java
                        )
                    if (response.body!!.response.activities != null) {
                        var responseActivities = response.body!!.response.activities!!
                        val sortedList = response.body!!.response.activities!!.sortedByDescending { it.period }
                        logger.trace("sortedList: first period ${sortedList.first()}- last period ${sortedList.last()}")
                        logger.trace("unsortedList: first period ${response.body!!.response.activities!!.first()} - last period ${response.body!!.response.activities!!.last()}")

                        if (sortedList == response.body!!.response.activities) {
                            logger.trace("Lists should be sorted the same")
                        } else {
                            responseActivities = sortedList
                        }

                        for (responseActivity in responseActivities) {
                            activities.add(Activity(instanceId = responseActivity.activityDetails.instanceId, date = responseActivity.period, modes = mutableListOf(), players = mutableListOf()))
                        }

                        if (responseActivities.last().period.isBefore(startDateTime)) {
                            logger.debug("${responseActivities.last().period} before $startDateTime not necessary to grab an additional Page")

                            nextPage = false
                        } else {
                            logger.debug("${responseActivities.last().period} after $startDateTime grabbing an additional Page")
                            currentPage += 1
                        }
                    } else {
                        logger.debug("Activity lookup returned no activities for forseth & $characterId")
                        nextPage = false
                    }
                }

            } catch (e: ResourceAccessException) {
                e.message?.let { logger.error(it, e) }
            }
            
        }
        
        logger.info("Found ${activities.size} Activities for forSeth")

    }
}