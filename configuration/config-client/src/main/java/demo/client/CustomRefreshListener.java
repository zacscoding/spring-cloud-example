package demo.client;

import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomRefreshListener {

    @EventListener
    public void refresh(RefreshScopeRefreshedEvent e) {
        logger.warn("refresh event occur. name : {} / source : {}", e.getName(), e.getSource());
    }
}
