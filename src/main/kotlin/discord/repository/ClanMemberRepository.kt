package discord.repository

import discord.model.dao.ClanMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClanMemberRepository : JpaRepository<ClanMember, Long> 