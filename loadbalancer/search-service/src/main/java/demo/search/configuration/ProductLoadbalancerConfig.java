package demo.search.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class ProductLoadbalancerConfig {

    @Bean
    public IRule roundRobinRule() {
        return new LoggingRoundRobinRule();
    }

    public static class LoggingRoundRobinRule extends RoundRobinRule {

        public Server choose(Object key) {
            final Server server = super.choose(key);
            logger.info("## Try to choose {} --> {}", key, server);
            return server;
        }

        public void setLoadBalancer(ILoadBalancer lb) {
            super.setLoadBalancer(lb);
            logger.info("## Configured loadbalancer : {}", lb == null ? "null" : lb.getClass().getName());
        }

        public ILoadBalancer getLoadBalancer() {
            return super.getLoadBalancer();
        }
    }
}
