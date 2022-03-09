package randy.vintrace.endpoints

import org.hamcrest.Matchers.*
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
class BreakdownEndpointTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val wineCellar: FakeWineCellar
) {
    @DisplayName("Test single wine component breakdown")
    @Test
    fun testSingleComponent()
    {
        wineCellar.addWine(
            Wine("FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2018, "Sauvignon Blanc", "Derwent Valley"),
                        100.0f))
            ))
        mockMvc.get("/api/breakdown/year/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", `is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", `is`(1)) }
            content { jsonPath<String>("$.breakdown[0].percentage", `is`("100"))}
            content { jsonPath<String>("$.breakdown[0].key", `is`("2018"))}
        }
    }

    @DisplayName("Test wine with two components")
    @Test
    fun testTwoComponents()
    {
        wineCellar.addWine(
            Wine("FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2018, "Sauvignon Blanc", "Derwent Valley"),
                        60.0f),
                    Pair(
                        WineComponent(2017, "Sauvignon Blanc", "Derwent Valley"),
                        40.0f))
            ))
        mockMvc.get("/api/breakdown/year/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", `is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", `is`(2)) }
            content { jsonPath<String>("$.breakdown[0].percentage", `is`("60"))}
            content { jsonPath<String>("$.breakdown[0].key", `is`("2018"))}
            content { jsonPath<String>("$.breakdown[1].percentage", `is`("40"))}
            content { jsonPath<String>("$.breakdown[1].key", `is`("2017"))}
        }
    }

    @DisplayName("Test that breakdowns are sorted by percentage in descending order")
    @Test
    fun testSortOrder()
    {
        wineCellar.addWine(
            Wine("FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2018, "Sauvignon Blanc", "Derwent Valley"),
                        49.0f),
                    Pair(
                        WineComponent(2017, "Sauvignon Blanc", "Derwent Valley"),
                        51.0f))
            ))
        mockMvc.get("/api/breakdown/year/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", `is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", `is`(2)) }
            content { jsonPath<String>("$.breakdown[0].percentage", `is`("51"))}
            content { jsonPath<String>("$.breakdown[0].key", `is`("2017"))}
            content { jsonPath<String>("$.breakdown[1].percentage", `is`("49"))}
            content { jsonPath<String>("$.breakdown[1].key", `is`("2018"))}
        }
    }

    @DisplayName("Test coalescing of components with same year")
    @Test
    fun testCoalescingWinesFromTheSameYear()
    {
        wineCellar.addWine(
            Wine("FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2022, "Cabernet Sauvignon", "Coonawarra"),
                        35.0f),
                    Pair(
                        WineComponent(2021, "Cabernet Sauvignon", "Coonawarra"),
                        25.0f),
                    Pair(
                        WineComponent(2022, "Shiraz", "McLaren Vale"),
                        10.0f),
                    Pair(
                        WineComponent(2021, "Shiraz", "Barossa Valley"),
                        30.0f))
            ))
        mockMvc.get("/api/breakdown/year/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", `is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", `is`(2)) }
            content { jsonPath<String>("$.breakdown[0].percentage", `is`("55"))}
            content { jsonPath<String>("$.breakdown[0].key", `is`("2021"))}
            content { jsonPath<String>("$.breakdown[1].percentage", `is`("45"))}
            content { jsonPath<String>("$.breakdown[1].key", `is`("2022"))}
        }
    }

    @DisplayName("Test that percentage rounding happens after coalescing components")
    @Test
    fun testLatePercentageRounding()
    {
        wineCellar.addWine(
            Wine("FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2022, "Cabernet Sauvignon", "Coonawarra"),
                        50.5f),
                    Pair(
                        WineComponent(2022, "Shiraz", "Barossa Valley"),
                        49.5f))
            ))
        mockMvc.get("/api/breakdown/year/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", `is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", `is`(1)) }

            // If rounding was done before coalescing, the percentage would be 101!
            content { jsonPath<String>("$.breakdown[0].percentage", `is`("100"))}
            content { jsonPath<String>("$.breakdown[0].key", `is`("2022"))}
        }
    }

    @DisplayName("Test that percentages always add up to 100%")
    @Test
    fun testPercentageApportionment()
    {
        wineCellar.addWine(
            Wine("FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2022, "Cabernet Sauvignon", "Coonawarra"),
                        50.5f),
                    Pair(
                        WineComponent(2021, "Shiraz", "Barossa Valley"),
                        49.5f))
            ))
        mockMvc.get("/api/breakdown/year/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", `is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", `is`(2)) }

            // Because the % increase in error from 50.5 to 51 is lower than the % increase in error from
            // 49.5 to 50, we expect the 2022 vintage to be rounded up to 51%
            content { jsonPath<String>("$.breakdown[0].percentage", `is`("51"))}
            content { jsonPath<String>("$.breakdown[0].key", `is`("2022"))}
            content { jsonPath<String>("$.breakdown[1].percentage", `is`("49"))}
            content { jsonPath<String>("$.breakdown[1].key", `is`("2021"))}
        }
    }

    @DisplayName("Test simpler rounding")
    @Test
    fun testBasicRounding()
    {
        wineCellar.addWine(
            Wine("FOOBAR",
                arrayOf(
                    Pair(
                        WineComponent(2022, "Cabernet Sauvignon", "Coonawarra"),
                        51.25f),
                    Pair(
                        WineComponent(2021, "Shiraz", "Barossa Valley"),
                        48.75f))
            ))
        mockMvc.get("/api/breakdown/year/FOOBAR") {
            contextPath = "/api"
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.breakDownType", `is`("year"))}
            content { jsonPath<Int>("$.breakdown.length()", `is`(2)) }

            content { jsonPath<String>("$.breakdown[0].percentage", `is`("51"))}
            content { jsonPath<String>("$.breakdown[0].key", `is`("2022"))}
            content { jsonPath<String>("$.breakdown[1].percentage", `is`("49"))}
            content { jsonPath<String>("$.breakdown[1].key", `is`("2021"))}
        }
    }
}