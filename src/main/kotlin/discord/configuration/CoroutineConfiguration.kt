package discord.configuration

import discord.component.AppCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineConfiguration(
    @Value("\${parallelism.threadCount:64}")
    val parallelismCount: Int
) {
    
    @Bean
    @OptIn(ExperimentalCoroutinesApi::class)
    fun scope(): AppCoroutineScope = AppCoroutineScope(parallelismCount) 
}
