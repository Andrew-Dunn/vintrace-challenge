package randy.vintrace.persistence

import randy.vintrace.model.Wine

/**
 * An interface that exposes methods for retrieving wine from a data store.
 *
 * Realistically I could have named this something like WinePersistence or WineStore,
 * but that would have been less fun.
 */
interface WineCellar {
    fun getWine(lotCode: String): Wine?
}
