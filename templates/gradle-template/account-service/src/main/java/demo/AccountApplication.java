package demo;

import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class AccountApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(AccountApplication.class, args);
        final ConfigurableEnvironment env = ctx.getEnvironment();
        final MutablePropertySources propertySources = env.getPropertySources();
        for (PropertySource<?> p : propertySources.stream().collect(Collectors.toList())) {
            System.out.println(p.getName() + " -> " + p.getSource());
        }
    }
}
