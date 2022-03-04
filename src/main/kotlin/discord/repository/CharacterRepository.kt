package discord.repository

import discord.model.dao.Character
import org.springframework.data.jpa.repository.JpaRepository

interface CharacterRepository: JpaRepository<Character, Long> 