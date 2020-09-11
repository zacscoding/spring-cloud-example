package demo.search.example;

import java.util.List;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import demo.search.helper.StackTrace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StaticServerList implements ServerList<Server> {

    private final List<Server> servers;

    public StaticServerList(List<Server> servers) {
        this.servers = servers;
    }

    @Override
    public List<Server> getInitialListOfServers() {
        logger.info("## called getInitialListOfServers()\n", StackTrace.getNetflixStackTrace());
        return servers;
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        logger.info("## called getUpdatedListOfServers()\n", StackTrace.getNetflixStackTrace());
        return servers;
    }
}
