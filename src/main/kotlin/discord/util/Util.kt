package discord.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class Util @Autowired constructor() {
    companion object {
        fun convertMillisToTimeStamp(milliSeconds: Long): String {
            return String.format(
                "%02d:%02d:%02d,%02d", milliSeconds / (3600 * 1000),
                milliSeconds / (60 * 1000) % 60,
                milliSeconds / 1000 % 60, milliSeconds % 1000
            )
        }
        
        fun createHttpEntity(apiKey: String): HttpEntity<String> {
            val headers = HttpHeaders()
            headers.set("X-API-Key", apiKey)
            
            return HttpEntity("body", headers)
        }
    }
}