package demo.search.ribbon;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListUpdater;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

import demo.search.example.LoggingServerListFilter;
import demo.search.example.Ping;
import demo.search.example.Rule;
import demo.search.example.StaticServerList;

public class LoadbalancerTest {

    @Test
    public void buildLoadBalancer() throws Throwable {
        final Server server1 = new Server("localhost", 3000);
        final Server server2 = new Server("localhost", 3100);
        final Predicate<Server> predicate = s -> {
            if (s.equals(server1)) {
                return true;
            }
            return new Random().nextInt(2) == 0;
        };
        final ServerListUpdater updater = new PollingServerListUpdater(TimeUnit.SECONDS.toMillis(5L),
                                                                       TimeUnit.SECONDS.toMillis(5L));

        final LoadBalancerBuilder<Server> builder = LoadBalancerBuilder.newBuilder();
        final ZoneAwareLoadBalancer<Server> lb = builder.withPing(new Ping(predicate))
                                                        .withRule(new Rule())
                                                        .withDynamicServerList(
                                                                new StaticServerList(Arrays.asList(server1, server2)))
                                                        .withServerListFilter(new LoggingServerListFilter())
                                                        .withServerListUpdater(updater)
                                                        .buildDynamicServerListLoadBalancer();

        TimeUnit.SECONDS.sleep(30L);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            System.out.println("Try to choose server.");
            final Server server = lb.chooseServer("key" + i);
            System.out.println(">> " + server);

            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                // ignore
            }
        });
    }
}
