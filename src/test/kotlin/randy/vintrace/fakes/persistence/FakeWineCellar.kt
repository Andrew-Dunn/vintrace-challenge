package randy.vintrace.fakes.persistence

import org.springframework.stereotype.Component
import randy.vintrace.model.Wine
import randy.vintrace.persistence.WineCellar

/**
 * Simple fake implementation of the WineCellar interface.
 * This wasn't strictly necessary, but it's a nice way to test the persistence layer without having to create
 * multiple JSON files.
 */
@Component
class FakeWineCellar: WineCellar {
    val wines: MutableMap<String, Wine> = HashMap()

    override fun getWine(lotCode: String): Wine? {
        return wines.getOrDefault(lotCode, null)
    }

    fun addWine(wine: Wine) {
        wines.put(wine.lotCode, wine)
    }
}
