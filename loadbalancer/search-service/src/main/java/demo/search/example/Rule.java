package demo.search.example;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

import demo.search.helper.StackTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Rule implements IRule {

    private ILoadBalancer loadBalancer;

    @Override
    public Server choose(Object key) {
        logger.info("## [Rule] choose({})\n{}", key, StackTrace.getNetflixStackTrace());
        return loadBalancer.getReachableServers().get(0);
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        this.loadBalancer = lb;
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return loadBalancer;
    }
}
