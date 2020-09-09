package demo.product;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final Environment env;
    private ServiceMetadata metadata;

    @PostConstruct
    private void setUp() {
        metadata = new ServiceMetadata();
        metadata.setInstanceId(env.getProperty("eureka.instance.instance-id"));
        metadata.setPort(Integer.parseInt(env.getProperty("server.port")));

    }

    @GetMapping("/api/v1/products")
    public ProductsResource getProducts() {
        final Random rand = new Random();
        final List<Product> products = IntStream.rangeClosed(1, 5)
                                                .boxed()
                                                .map(i -> Product.builder()
                                                                 .id(Long.valueOf(i))
                                                                 .name("product-" + i)
                                                                 .price(rand.nextInt(10000))
                                                                 .stockQuantity(rand.nextInt(10))
                                                                 .build()).collect(Collectors.toList());
        return ProductsResource.builder().metadata(metadata).products(products).build();
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
