package randy.vintrace.endpoints

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException
import randy.vintrace.endpoints.models.WineInfo
import randy.vintrace.persistence.WineCellar
import java.text.NumberFormat
import java.util.*

@Controller
@RequestMapping("/wine")
class WineEndpoint (
    val cellar: WineCellar
) {
    @GetMapping("/{lotCode}")
    @ResponseBody
    fun getWine(@PathVariable lotCode: String, userLocale: Locale): WineInfo {
        val wine = cellar.getWine(lotCode) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Wine '$lotCode' not found."
        )

        // This is some basic localisation for the sake of this demo, in reality it would be done far more extensively.
        val numberFormatter = NumberFormat.getIntegerInstance(userLocale)
        val volumeString: String? = if (wine.volume != null) {
            numberFormatter.format(wine.volume) + " L"
        } else {
            null
        }

        return WineInfo(
            wine.lotCode,
            wine.description, volumeString, wine.tankCode, wine.productState, wine.ownerName
        )
    }
}