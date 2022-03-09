package randy.vintrace.persistence.fs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.stereotype.Component
import randy.vintrace.model.Wine
import randy.vintrace.persistence.WineCellar

@Component
class FileSystemWineCellar : WineCellar {
    lateinit var wineCellarPath: String
    private final val objectMapper: ObjectMapper = ObjectMapper()

    init {
        val module = SimpleModule()
        module.addDeserializer(Wine::class.java, WineDeserialiser())
        objectMapper.registerModule(module)
    }

    override fun getWine(lotCode: String): Wine? {
        val resource = javaClass.getResource(wineCellarPath + lotCode + ".json") ?: return null
        return objectMapper.readValue(resource, Wine::class.java)
    }
}