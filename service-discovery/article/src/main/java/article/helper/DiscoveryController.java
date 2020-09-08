package article.helper;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/discovery")
@RequiredArgsConstructor
public class DiscoveryController {
    private final DiscoveryClient discoveryClient;

    @GetMapping("/{serviceId}")
    public List<ServiceInstance> discovery(@PathVariable("serviceId") String serviceId) {
        logger.warn("discovery {}", serviceId);
        return discoveryClient.getInstances(serviceId);
    }

    @GetMapping("/services")
    public List<String> discoveryServices() {
        final List<String> services = discoveryClient.getServices();
        logger.warn("services {}", String.join(",", services));
        return services;
    }
}
