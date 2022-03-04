package discord.model.response.historicalstats

import discord.model.response.clan.UserInfoCard

data class DestinyPlayer(
    val destinyUserInfo: UserInfoCard,
    val characterClass: String?,
    val classHash: String?,
    val raceHash: String?,
    val genderHash: String?,
    val characterLevel: Int,
    val lightLevel: Int,
    val bungieNetUserInfo: UserInfoCard?,
    val clanName: String?,
    val clanTag: String?,
    val emblemHash: String?
)
