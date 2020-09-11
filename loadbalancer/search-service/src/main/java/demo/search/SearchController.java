package demo.search;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SearchController {

    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate restTemplate;
    private final RestTemplate loadBalanced;

    public SearchController(DiscoveryClient discoveryClient,
                            LoadBalancerClient loadBalancerClient,
                            @Qualifier("restTemplate") RestTemplate restTemplate,
                            @Qualifier("loadBalanced") RestTemplate loadBalanced) {
        this.discoveryClient = discoveryClient;
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
        this.loadBalanced = loadBalanced;
    }

    @GetMapping("/search/load-balancer-client")
    public ResponseEntity<ProductsResource> search() {
        final ServiceInstance instance = loadBalancerClient.choose("product-service");
        logger.info("## request /search/load-balancer-client -> Choose service : {}", instance);
        return searchInternal(instance.getUri().toString(), restTemplate);
    }

    @GetMapping("/search/ribbon")
    public ResponseEntity<ProductsResource> searchByRibbon() {
        return searchInternal("http://product-service", loadBalanced);
    }

    private ResponseEntity<ProductsResource> searchInternal(String url, RestTemplate template) {
        final URI uri = UriComponentsBuilder.fromHttpUrl(url)
                                            .pathSegment("api", "v1", "products")
                                            .build().toUri();
        final ResponseEntity<ProductsResource> result = template.getForEntity(uri, ProductsResource.class);

        if (result.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(result.getBody());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.getBody());
    }

    //////// To discovery info

    @GetMapping("/discovery/services")
    public Map<String, List<ServiceInstance>> discoveryServices() {
        return discoveryClient.getServices()
                              .stream()
                              .collect(Collectors.toMap(s -> s, discoveryClient::getInstances));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ProductsResource {
        private ServiceMetadata metadata;
        private List<Product> products;
    }

    @Data
    static class ServiceMetadata {
        private String instanceId;
        private Integer port;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class Product {

        private Long id;
        private String name;
        private int price;
        private int stockQuantity;
    }
}
