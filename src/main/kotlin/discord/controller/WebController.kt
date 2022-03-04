package discord.controller

import discord.model.dao.ClanMember
import discord.model.response.enum.DestinyActivityModeType
import discord.repository.ClanMemberRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController(private val memberRepository: ClanMemberRepository) {

    @GetMapping("/")
    fun result(model: Model): String {
        model["title"] = "Results"
        model["clanMembers"] = memberRepository.findAll().map { it.render() }
        return "result"
    }

    fun ClanMember.render() = RenderedClanMember(
        displayName,
        bungieDisplayName,
        membershipId,
        activityCounter.activitySum,
        activityCounter.activitiesByMode
    )

    data class RenderedClanMember(
        val displayName: String,
        val bungieDisplayName: String,
        val membershipId: Long,
        val activitySum: Long,
        val activityByMode:  MutableMap<DestinyActivityModeType, Long>
    )
}