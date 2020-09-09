package demo.product;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ProductApplication {
    public static void main(String[] args) {
        waitOtherServices();
        SpringApplication.run(ProductApplication.class, args);
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
}
