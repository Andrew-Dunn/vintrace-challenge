package randy.vintrace.endpoints

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import randy.vintrace.controllers.WineCollator
import randy.vintrace.controllers.WineComponentCategoriser
import randy.vintrace.endpoints.models.BreakdownResponse
import randy.vintrace.endpoints.models.BreakdownType
import randy.vintrace.model.Wine
import randy.vintrace.persistence.WineCellar
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/breakdown")
class BreakdownEndpoint (
    val cellar: WineCellar
) {
    @GetMapping("/{breakdownType}/{lotCode}")
    @ResponseBody
    fun getYearBreakdown(response: HttpServletResponse,
                         @PathVariable breakdownType: String,
                         @PathVariable lotCode: String): BreakdownResponse {
        val wine: Wine = cellar.getWine(lotCode) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wine '$lotCode' not found.")
        val categoriser: WineComponentCategoriser = when (breakdownType) {
            BreakdownType.YEAR.key -> WineComponentCategoriser.YEAR
            BreakdownType.VARIETY.key -> WineComponentCategoriser.VARIETY
            BreakdownType.REGION.key -> WineComponentCategoriser.REGION
            BreakdownType.YEAR_VARIETY.key -> WineComponentCategoriser.YEAR_AND_VARIETY
            BreakdownType.REGION_VARIETY.key -> WineComponentCategoriser.REGION_AND_VARIETY
            else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid breakdown type '$breakdownType'.")
        }
        val collator = WineCollator(categoriser)
        return BreakdownResponse(BreakdownType.YEAR, collator.collateComponents(wine))
    }
}