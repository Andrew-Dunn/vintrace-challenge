package randy.vintrace.endpoints.models

/**
 * Response type of the /api/wines/{lotCode} endpoint.
 *
 * The main design drivers here was minimising the amount of processing the client needs to do with the information
 * returned. That for instance, is why "null" string values are returned as actual null values, and why the volume
 * is returned as a pre-formatted string.
 */
data class WineInfo (
    val lotCode: String,
    val description: String?,
    val volume: String?,
    val tankCode: String?,
    val productState: String?,
    val owner: String?,
)