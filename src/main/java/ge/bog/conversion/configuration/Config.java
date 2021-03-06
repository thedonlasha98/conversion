package ge.bog.conversion.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Config {
    @Bean
    public RestTemplate getRestTemplate(
            RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(1000))
                .setReadTimeout(Duration.ofSeconds(1000))
                .build();
    }
}
