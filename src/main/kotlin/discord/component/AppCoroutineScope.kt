package discord.component

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class AppCoroutineScope(private val parallelismCount: Int): CoroutineScope by CoroutineScope(Dispatchers.Default.limitedParallelism(parallelismCount))