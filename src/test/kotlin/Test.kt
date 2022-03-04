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
class Test: Logging {
    private val activitiesEndpoint = "/Destiny2/3/Account//Character/2305843009527196770/Stats/Activities/"
    private val groupID = "3351106"
    private val baseUrl = "https://www.bungie.net/platform/"
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
    fun testActivityApi() {
        val response = restTemplate.exchange("$baseUrl$activitiesEndpoint", HttpMethod.GET, entity, GetActivityHistory::class.java)

        logger.info(response)

        response.body!!.response.activities?.size?.let { logger.info(it) }

    }

    @Test
    fun blah() {
        val response = restTemplate.exchange(
            "https://www.bungie.net/platform/Destiny2/3/Account/4611686018493410000/Character/2305843009527196700/Stats/Activities/?count=50&page=1",
            HttpMethod.GET,
            entity,
            GetActivityHistory::class.java
        )

        val startDate = LocalDateTime.parse("2022-01-01T00:00:00")

        logger.info(response)

        response.body!!.response.activities?.size?.let { logger.info(it) }

        if (response.body!!.response.activities!!.last().period.isBefore(startDate))
            logger.info("Should grab another page")

    }

    @Test
    fun testActivityApiByPage() {
        var currentPage = 1
        val response = restTemplate.exchange("$baseUrl$activitiesEndpoint?page=$currentPage", HttpMethod.GET, entity, GetActivityHistory::class.java)

        logger.info(response)

        logger.info(response.body!!.response.activities!!.size)

        var lastDate = response.body!!.response.activities!!.last().period
        while (!lastDate.isBefore(LocalDateTime.of(2022, 1, 1, 0, 0, 0, 0))) {
            currentPage += 1
            val response2 = restTemplate.exchange("$baseUrl$activitiesEndpoint?page=$currentPage", HttpMethod.GET, entity, GetActivityHistory::class.java)
            logger.info("Page2: $response2")
            lastDate = response2.body!!.response.activities!!.last().period
            logger.info(lastDate)
        }
    }

    @Test
    fun getActivityByPageWithLoopCode() {
        val displayName = "forSeth"
        val characterId = "2305843009730174645"
        val rowCount = 100
        var currentPage = 1
//        val activitiesEndpoint = "https://www.bungie.net/platform/Destiny2/3/Account/$memberShipId/Character/$characterId/Stats/Activities/"
        var activities = 0
        val startDateTime = LocalDateTime.parse("2022-01-01T00:00:00")

        var nextPage = true
        try {
            while (nextPage) {
                logger.debug("Getting Page $currentPage for $displayName's characterId: $characterId")
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
                        logger.debug("Lists should be sorted the same")
                    } else {
                        responseActivities = sortedList
                    }

                    activities += responseActivities.size

                    if (responseActivities.last().period.isBefore(startDateTime)) {
                        nextPage = false
                    } else {
                        logger.debug("${responseActivities.last().period} before $startDateTime grabbing an additional Page")
                        currentPage += 1
                    }
                } else {
                    logger.debug("Activity lookup returned no activities for $displayName & $characterId")
                    nextPage = false
                }
            }
        } catch (e: ResourceAccessException) {
            e.message?.let { logger.error(it, e) }
            throw e
        }

        logger.info("Acquired $activities Activities for $displayName")
    }
}
