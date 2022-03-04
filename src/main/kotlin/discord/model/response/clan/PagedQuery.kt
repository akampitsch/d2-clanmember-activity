package discord.model.response.clan

data class PagedQuery(
    val itemsPerPage: Int,
    val currentPage: Int,
    val requestContinuationToken: String?
)
