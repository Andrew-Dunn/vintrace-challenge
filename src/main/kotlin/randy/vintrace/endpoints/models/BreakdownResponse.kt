package randy.vintrace.endpoints.models

import randy.vintrace.model.WineComponent

data class BreakdownResponse(
    val breakDownType: BreakdownType,
    val breakdown: Array<BreakdownElement>
) {

    constructor(breakDownType: BreakdownType, collatedComponents: Map<String, Map<WineComponent, Float>>)
            : this(breakDownType, getBreakdownElementsFromCollation(collatedComponents))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BreakdownResponse

        if (breakDownType != other.breakDownType) return false
        if (!breakdown.contentEquals(other.breakdown)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = breakDownType.hashCode()
        result = 31 * result + breakdown.contentHashCode()
        return result
    }
}

/**
 * This method converts the raw collation results into a REST response.
 * It sums any matching entries, sorts them in descending order by volume,
 * and rounds all percentages to the nearest integer.
 */
fun getBreakdownElementsFromCollation(collatedComponents: Map<String, Map<WineComponent, Float>>): Array<BreakdownElement> {
    val percentages = collatedComponents
        .map { entry -> Pair(entry.key, entry.value.values.sum())}
        .toTypedArray()

    // For apportionment, we will round all percentages down to the nearest integer, then add 1 percentage point to
    // the components which would introduce the least additional error, until we have 100%.
    // As a side effect, we will also correct instances where the sum of the percentages was already less than 100%.

    // This code is pretty gross, if I had more time I would refactor it by creating descriptive data classes rather
    // than using Pairs and Triplets.

    // For some reason the Float type in Kotlin isn't supported by the sumOf() function, so we need to do it manually.
    val total = percentages.map { it.second }.sum()
    val roundedPercentages = percentages.map { Triple(
        it.first, 100.0 * it.second / total, (100.0 * it.second / total).toInt()) }.toTypedArray()
    val roundedTotal = roundedPercentages.sumOf { it.third }

    val apportionedPercentages = roundedPercentages
        // Calculate the error in the rounded percentages, a lower value indicates that rounding up introduces less
        // error.
        .map { Triple(it.first, it.second, ((it.third + 1 - it.second)) / it.second) }
        // Sort by error delta, ascending.
        .sortedBy { it.third }
        // Add 1 percentage point to the components with the lowest rounded up error delta until we have 100%.
        .mapIndexed { index, triple -> Pair(
            triple.first, triple.second.toInt() + (if (index < (100 - roundedTotal)) 1 else 0))}
        // Re-sort by rounded percentage, descending.
        .sortedByDescending { it.second }.toTypedArray()

    // This is where I would properly apportion the percentages to add up to 100%.
    // For now, I'm just rounding to the nearest integer.
    return apportionedPercentages
        .map { pair -> BreakdownElement(pair.second.toString(), pair.first)}
        .toTypedArray()
}
