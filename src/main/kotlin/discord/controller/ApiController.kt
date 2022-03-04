package discord.controller

import discord.component.AppCoroutineScope
import discord.service.ActivityService
import discord.service.ClanMemberService
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Controller
class ApiController @Autowired constructor(val scope: AppCoroutineScope, val memberService: ClanMemberService, val activityService: ActivityService) : Logging {
    @GetMapping("/members")
    suspend fun getMembers(): ResponseEntity<HttpStatus> {
        logger.info("Received GET /members Request - Processing")
        scope.launch { memberService.getAndStoreMembers() }

        return ResponseEntity(HttpStatus.ACCEPTED)

//        try {
//            val members: MutableList<ClanMember> = mutableListOf()
//
//            val timeInMillis = measureTimeMillis {
//                members.addAll(memberService.getAndStoreMembers())
//            }
//
//            logger.info("Gathered Members in ${Util.convertMillisToTimeStamp(timeInMillis)}")
//
//            if (members.isEmpty()) {
//                return ResponseEntity<List<ClanMember>>(HttpStatus.NO_CONTENT)
//            }
//
//            return ResponseEntity<List<ClanMember>>(members, HttpStatus.OK)
//        } catch (e: Exception) {
//            e.message?.let { logger.error(it, e) }
//            return ResponseEntity<List<ClanMember>>(HttpStatus.INTERNAL_SERVER_ERROR)
//        }
    }

    @GetMapping("/activities")
    fun getActivities(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDateTime: LocalDateTime): ResponseEntity<HttpStatus> {
        logger.info("Received GET /activities Request with startDate=$startDateTime - Processing")

        val members = memberService.getMembers()

        scope.launch {
            activityService.getAndStoreActivities(members, startDateTime)
        }
        
        return ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED)
    }
}