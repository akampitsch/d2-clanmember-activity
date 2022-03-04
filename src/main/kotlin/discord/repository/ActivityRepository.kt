package discord.repository

import discord.model.dao.Activity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivityRepository: JpaRepository<Activity, Long> {
    fun findByInstanceId(instanceId: Long): Activity?
}
