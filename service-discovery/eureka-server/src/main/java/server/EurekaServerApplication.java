package server;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Predicate;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableEurekaServer
@RestController
public class EurekaServerApplication {
    public static void main(String[] args) {
        logger.warn("## Args {}", Arrays.toString(args));
        final ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EurekaServerApplication.class)
                .web(WebApplicationType.SERVLET).run(args);
        // displayBeans(ctx);
    }

    private static void displayBeans(ConfigurableApplicationContext ctx) {
        final Predicate<String> predicate = className -> {
            if (className.startsWith("org.springframework.cloud.netflix")) {
                return true;
            }
            if (className.startsWith("com.netflix")) {
                return true;
            }
            return false;
        };

        final TreeMap<String, String> treeMap = new TreeMap<>();
        for (String beanName : ctx.getBeanDefinitionNames()) {
            final Class<?> clazz = ctx.getBean(beanName).getClass();
            if (predicate.test(clazz.getName())) {
                treeMap.put(clazz.getSimpleName(), beanName);
            }
        }

        final StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : treeMap.entrySet()) {
            sb.append('[').append(entry.getValue()).append("] : ").append(entry.getKey()).append('\n');
        }

        logger.warn("## Display beans\n{}", sb.toString());
    }

    @GetMapping
    public String sayHello() {
        return "hello";
    }
}
