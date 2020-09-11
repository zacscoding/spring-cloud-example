package demo.search;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SearchApplication {
    public static void main(String[] args) {
        waitOtherServices();
        final ConfigurableApplicationContext ctx = SpringApplication.run(SearchApplication.class, args);
        displayBeans(ctx);
    }

    static void waitOtherServices() {
        final String waitSec = System.getenv("STARTUP_WAIT");
        logger.info("Wait other services with {} secs.", waitSec);
        if (StringUtils.hasText(waitSec)) {
            try {
                TimeUnit.SECONDS.sleep(Integer.parseInt(waitSec));
            } catch (InterruptedException e) {
                logger.warn("Failed to wait", e);
            }
        }
    }

    static void displayBeans(ConfigurableApplicationContext ctx) {
        final Predicate<String> predicate = className -> {
            if (className.startsWith("org.springframework.cloud")) {
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
                treeMap.put(clazz.getName(), beanName);
            }
        }

        final StringBuilder sb = new StringBuilder();
        for (String key : treeMap.navigableKeySet()) {
            sb.append('[').append(treeMap.get(key)).append("] : ").append(key).append('\n');
        }
        logger.warn("## Display beans\n{}", sb.toString());
    }
}
