package randy.vintrace.model

data class Wine (
    val lotCode: String,

    // We cannot use a Map type here, because it is possible for multiple components to have the exact
    // same properties. A Pair was used to represent the percentage of the component, as it is more a property of the
    // wine than the component itself.
    val components: Array<Pair<WineComponent, Float>>,
    val description: String? = null,
    val volume: Double? = null,
    val tankCode: String? = null,
    val productState: String? = null,
    val ownerName: String? = null
)