package demo.search.example;

import java.util.function.Predicate;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;

import demo.search.helper.StackTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Ping implements IPing {

    private final Predicate<Server> predicate;

    public Ping(Predicate<Server> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean isAlive(Server server) {
        logger.info("## called isAlive({})\n{}", server, StackTrace.getNetflixStackTrace());
        return predicate.test(server);
    }
}
