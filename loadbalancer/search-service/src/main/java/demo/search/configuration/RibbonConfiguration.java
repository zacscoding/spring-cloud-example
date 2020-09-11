package demo.search.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RibbonClients({
        @RibbonClient(name = "product-service", configuration = { ProductLoadbalancerConfig.class })
})
public class RibbonConfiguration {
}
