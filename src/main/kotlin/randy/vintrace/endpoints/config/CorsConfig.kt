package randy.vintrace.endpoints.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Because the frontend runs on a different server than the backend, we need to enable CORS.
 */
@Configuration
@EnableWebMvc
class CorsConfig: WebMvcConfigurer{
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
    }
}