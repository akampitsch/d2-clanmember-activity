package discord.model.response.clan

data class SearchResultOfGroupMember(
    val results: List<Member>,
    val totalResults: Int,
    val hasMore: Boolean,
    val query: PagedQuery,
    val replacementContinuationToken: String?,
    val useTotalResults: Boolean
)
