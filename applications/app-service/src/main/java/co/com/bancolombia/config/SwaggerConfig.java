package co.com.bancolombia.config;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringDocConfiguration.class)
public class SwaggerConfig {
}
