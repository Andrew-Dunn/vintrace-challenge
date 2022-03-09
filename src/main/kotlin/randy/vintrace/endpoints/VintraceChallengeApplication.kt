package randy.vintrace.endpoints

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource("classpath:beans.xml")
class VintraceChallengeApplication

fun main(args: Array<String>) {
	runApplication<VintraceChallengeApplication>(*args)
}
