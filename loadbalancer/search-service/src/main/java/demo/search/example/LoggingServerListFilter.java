package demo.search.example;

import java.util.List;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListFilter;

import demo.search.helper.StackTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingServerListFilter implements ServerListFilter<Server> {

    @Override
    public List<Server> getFilteredListOfServers(List<Server> servers) {
        logger.info("## called getFilteredListOfServers()\n{}", StackTrace.getNetflixStackTrace());
        return servers;
    }
}
