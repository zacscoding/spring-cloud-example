package demo.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SearchConfiguration {

    @Bean
    public RestTemplate defaultRestTemplate() {
        return new RestTemplate();
    }
}
