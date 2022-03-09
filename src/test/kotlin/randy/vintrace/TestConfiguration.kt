package randy.vintrace

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import randy.vintrace.fakes.persistence.FakeWineCellar

@org.springframework.boot.test.context.TestConfiguration
class TestConfiguration {
    private val wineCellarImpl = FakeWineCellar()

    @Bean(name = ["fakeWineCellar"])
    @Primary
    fun getFakeWineCellar(): FakeWineCellar {
        return wineCellarImpl
    }
}