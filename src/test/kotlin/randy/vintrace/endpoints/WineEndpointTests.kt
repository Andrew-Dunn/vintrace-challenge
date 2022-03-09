package randy.vintrace.endpoints

import org.hamcrest.Matchers
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import randy.vintrace.TestConfiguration
import randy.vintrace.fakes.persistence.FakeWineCellar
import randy.vintrace.model.Wine
import randy.vintrace.model.WineComponent

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration::class)
@AutoConfigureMockMvc
class WineEndpointTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val wineCellar: FakeWineCellar
) {
    @DisplayName("Test single wine component breakdown")
    @Test
    fun testSingleComponent() {
        wineCellar.addWine(
            Wine(
                "FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2021, "Sauvignon Blanc", "Foobar Valley"),
                        100.0f
                    )
                ),
                "Test wine",
                1000.0,
                "M1-Abrams MBT",
                "Newly Single",
                "Foobar Valley Wines"
            )
        )
        mockMvc.get("/api/wine/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath<String>("$.lotCode", Matchers.`is`("FOOBAR"))}
            content { jsonPath<String>("$.description", Matchers.`is`("Test wine"))}
            content { jsonPath<String>("$.volume", Matchers.`is`("1,000 L"))}
            content { jsonPath<String>("$.tankCode", Matchers.`is`("M1-Abrams MBT"))}
            content { jsonPath<String>("$.productState", Matchers.`is`("Newly Single"))}
            content { jsonPath<String>("$.owner", Matchers.`is`("Foobar Valley Wines"))}
        }
    }

    @DisplayName("Test wrong lot code returns 404")
    @Test
    fun testNotFound() {
        wineCellar.addWine(
            Wine(
                "FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2018, "Sauvignon Blanc", "Derwent Valley"),
                        100.0f
                    )
                )
            )
        )
        mockMvc.get("/api/wine/FOOBAZ") {
            contextPath = "/api"
        }.andExpect {
            status { isNotFound() }
        }
    }

    @DisplayName("Test null values are allowed")
    @Test
    fun testNulls() {
        wineCellar.addWine(
            Wine(
                "FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2021, "Sauvignon Blanc", "Foobar Valley"),
                        100.0f
                    )
                ),
                null,
                null,
                null,
                null,
                null
            )
        )
        mockMvc.get("/api/wine/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath<String>("$.lotCode", Matchers.`is`("FOOBAR"))}
            content { jsonPath<Any>("$.description", Matchers.`is`(nullValue()))}
            content { jsonPath<Any>("$.volume", Matchers.`is`(nullValue()))}
            content { jsonPath<Any>("$.tankCode", Matchers.`is`(nullValue()))}
            content { jsonPath<Any>("$.productState", Matchers.`is`(nullValue()))}
            content { jsonPath<Any>("$.owner", Matchers.`is`(nullValue()))}
        }
    }
}
