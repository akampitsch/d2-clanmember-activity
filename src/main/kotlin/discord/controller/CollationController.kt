package discord.controller

import discord.component.AppCoroutineScope
import discord.service.ActivityService
import discord.service.CollationService
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class CollationController @Autowired constructor(private val scope: AppCoroutineScope, val activityService: ActivityService, val collationService: CollationService) : Logging {
    @GetMapping("/collate")
    suspend fun performCollation(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDateTime: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDateTime: LocalDateTime
    ): ResponseEntity<HttpStatus> {
        logger.info("Received GET /collate Request - Processing")

        scope.launch { collationService.runCollation(activityService.getActivities(), startDateTime, endDateTime) }

        return ResponseEntity(HttpStatus.ACCEPTED)
    }
}