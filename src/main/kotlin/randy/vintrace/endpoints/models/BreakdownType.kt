package randy.vintrace.endpoints.models

import com.fasterxml.jackson.annotation.JsonValue

enum class BreakdownType(@JsonValue val key: String) {
    YEAR("year"),
    VARIETY("variety"),
    REGION("region"),
    YEAR_VARIETY("year-variety"),
    REGION_VARIETY("region-variety"),
}
