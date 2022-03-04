package discord.model.response.clan

import java.time.LocalDateTime

data class Member(
    val memberType: Int,
    val isOnline: Boolean,
    val lastOnlineStatusChange: Int,
    val groupId: Int,
    val destinyUserInfo: GroupUserInfoCard,
    val bungieNetUserInfo: UserInfoCard?,
    val joinDate: LocalDateTime
)
