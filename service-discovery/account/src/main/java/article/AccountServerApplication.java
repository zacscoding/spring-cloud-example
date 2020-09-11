package article;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@RestController
public class AccountServerApplication {
    public static void main(String[] args) {
        logger.warn("start account server with : {}", Arrays.toString(args));
        waitOtherServices();
        SpringApplication.run(AccountServerApplication.class, args);
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

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/discovery/{serviceId}")
    public List<ServiceInstance> discovery(@PathVariable("serviceId") String serviceId) {
        logger.warn("discovery {}", serviceId);
        return discoveryClient.getInstances(serviceId);
    }

    @GetMapping("/discovery/services")
    public Map<String, List<ServiceInstance>> discoveryServices() {
        return discoveryClient.getServices()
                              .stream()
                              .collect(Collectors.toMap(s -> s, s -> discoveryClient.getInstances(s)));
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable("accountId") Long id) {
        logger.info("## account service. getAccount({}) is called", id);
        return ResponseEntity.ok(Account.builder().id(id).name(UUID.randomUUID().toString()).build());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Account {
        Long id;
        String name;
    }
}
