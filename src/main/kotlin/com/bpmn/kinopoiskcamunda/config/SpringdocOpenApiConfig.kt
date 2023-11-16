import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springdoc.core.GroupedOpenApi

@Configuration
class SpringdocOpenApiConfig {
    @Bean
    fun customOpenAPI(): GroupedOpenApi {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build()
    }
}