package randy.vintrace.endpoints

import org.hamcrest.Matchers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * Basic tests for the FileSystemWineCellar. We tested our main logic in the BreakdownEndpointTests class, so we just
 * need to test that the endpoint is working.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FileSystemWineCellarTests(
    @Autowired val mockMvc: MockMvc
) {
    @DisplayName("Test a simple year breakdown using the file system wine cellar")
    @Test
    fun testSingleComponent()
    {
        mockMvc.get("/api/breakdown/year/11YVCHAR001") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", Matchers.`is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", Matchers.`is`(2)) }
            content { jsonPath<String>("$.breakdown[0].percentage", Matchers.`is`("85"))}
            content { jsonPath<String>("$.breakdown[0].key", Matchers.`is`("2011"))}
            content { jsonPath<String>("$.breakdown[1].percentage", Matchers.`is`("15"))}
            content { jsonPath<String>("$.breakdown[1].key", Matchers.`is`("2010"))}
        }
    }

    @DisplayName("Test retrieving a wine's information using the file system wine cellar")
    @Test
    fun testGetWine()
    {
        mockMvc.get("/api/wine/11YVCHAR001") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath<String>("$.lotCode", Matchers.`is`("11YVCHAR001"))}
            content { jsonPath<String>("$.description", Matchers.`is`("2011 Yarra Valley Chardonnay"))}
            content { jsonPath<String>("$.volume", Matchers.`is`("1,000 L"))}
            content { jsonPath<String>("$.tankCode", Matchers.`is`("T25-01"))}
            content { jsonPath<String>("$.productState", Matchers.`is`("Ready for bottling"))}
            content { jsonPath<String>("$.owner", Matchers.`is`("YV Wines Pty Ltd"))}
        }
    }

    @DisplayName("Test retrieivng a wine with null values")
    @Test
    fun testGetWineWithNulls()
    {
        mockMvc.get("/api/wine/11YVCHAR002") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath<String>("$.lotCode", Matchers.`is`("11YVCHAR002"))}
            content { jsonPath<Any>("$.description", Matchers.`is`(Matchers.nullValue()))}
            content { jsonPath<String>("$.volume", Matchers.`is`("5,077 L"))}
            content { jsonPath<String>("$.tankCode", Matchers.`is`("T25-06"))}
            content { jsonPath<Any>("$.productState", Matchers.`is`(Matchers.nullValue()))}
            content { jsonPath<String>("$.owner",
                Matchers.`is`("YV Wines P/L and Vintage Kerr Joint Venture"))}
        }
    }

    @DisplayName("Test retrieivng a wine that is not in the file system wine cellar")
    @Test
    fun testWineNotInCellar()
    {
        mockMvc.get("/api/wine/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isNotFound() }
        }
    }
}
