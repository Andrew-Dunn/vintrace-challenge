package randy.vintrace.controllers

import randy.vintrace.model.WineComponent

interface WineComponentCategoriser {
    companion object {
        val YEAR: WineComponentCategoriser = YearCategoriser()
        val VARIETY: WineComponentCategoriser = VarietyCategoriser()
        val REGION: WineComponentCategoriser = RegionCategoriser()
        val YEAR_AND_VARIETY: WineComponentCategoriser = YearAndVarietyCategoriser()
        val REGION_AND_VARIETY: WineComponentCategoriser = RegionAndVarietyCategoriser()
    }
    fun categorise(wineComponent: WineComponent): String
}

private class YearCategoriser : WineComponentCategoriser {
    override fun categorise(wineComponent: WineComponent): String {
        return wineComponent.year.toString()
    }
}

private class VarietyCategoriser : WineComponentCategoriser {
    override fun categorise(wineComponent: WineComponent): String {
        return wineComponent.variety
    }
}

private class RegionCategoriser : WineComponentCategoriser {
    override fun categorise(wineComponent: WineComponent): String {
        return wineComponent.region
    }
}

private class YearAndVarietyCategoriser : WineComponentCategoriser {
    override fun categorise(wineComponent: WineComponent): String {
        return wineComponent.year.toString() + " - " + wineComponent.variety
    }
}

// I know nobody asked for this one but I thought it would be fun to add it,
// and it's quite easy to do so.
private class RegionAndVarietyCategoriser : WineComponentCategoriser {
    override fun categorise(wineComponent: WineComponent): String {
        return wineComponent.region + " " + wineComponent.variety
    }
}