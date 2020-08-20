package demo.client;

import org.springframework.cloud.bus.BusAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Profile이 "rabbitmq"인 경우만 {@link BusAutoConfiguration}를 활성화
 */
@Profile("rabbitmq")
@Configuration
@Import(BusAutoConfiguration.class)
public class BusConfiguration {
}
