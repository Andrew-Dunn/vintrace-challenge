package randy.vintrace.controllers

import randy.vintrace.model.Wine
import randy.vintrace.model.WineComponent

class WineCollator (
    val categoriser: WineComponentCategoriser
) {
    fun collateComponents(wine: Wine): Map<String, Map<WineComponent, Float>> {
        val outputMap: MutableMap<String, MutableMap<WineComponent, Float>> = HashMap()
        wine.components.forEach {
            val category = categoriser.categorise(it.first)
            val percentageMap: MutableMap<WineComponent, Float> = outputMap.getOrPut(category) { HashMap() }
            var newPercentage: Float = percentageMap.getOrDefault(it.first, 0.0f)
            newPercentage += it.second
            percentageMap[it.first] = newPercentage
        }
        return outputMap
    }
}