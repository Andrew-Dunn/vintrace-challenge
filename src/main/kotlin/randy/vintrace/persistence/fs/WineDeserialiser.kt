package randy.vintrace.persistence.fs

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import randy.vintrace.model.Wine
import randy.vintrace.model.WineComponent

/**
 * Since our Wine model is slightly different from the JSON we are receiving, we need to override the default
 * deserialiser.
 */
class WineDeserialiser : StdDeserializer<Wine>(null as Class<Wine>?) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Wine {
        val tree: JsonNode = p!!.codec.readTree(p)
        val lotCode = tree.get("lotCode").asText()
        val components = mutableListOf<Pair<WineComponent, Float>>()
        for (element in tree.get("components").elements())
        {
            val percentage = element.get("percentage").asDouble().toFloat()
            val year = element.get("year").asInt()
            val variety = element.get("variety").asText()
            val region = element.get("region").asText()
            components.add(Pair(WineComponent(year, variety, region), percentage))
        }
        val description = notNullString(tree.get("description").asText())
        val rawVolume = tree.get("volume").asDouble(Double.MIN_VALUE)
        val tankCode = notNullString(tree.get("tankCode").asText())
        val productState = notNullString(tree.get("productState").asText())
        val ownerName = notNullString(tree.get("ownerName").asText())
        val volume = if (rawVolume != Double.MIN_VALUE) rawVolume else null

        return Wine(lotCode, components.toTypedArray(), description, volume, tankCode, productState, ownerName)
    }
}

// For some reason the JSON files can have string values that are literally "null", this isn't great as it means
// users will have to see a lot of nulls in the UI, so we'll replace them with actual nulls, so that they can be hidden
// in the UI.
fun notNullString(string: String?): String? {
    return if (string != "null") string else null
}