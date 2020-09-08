package server.helper;

import org.springframework.cloud.netflix.eureka.server.InstanceRegistry;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EurekaEventListener {

    /**
     * Logging published events from {@link InstanceRegistry}
     */
    @EventListener({
            EurekaInstanceRegisteredEvent.class, EurekaInstanceRenewedEvent.class,
            EurekaInstanceCanceledEvent.class
    })
    public void onStateChanges(Object e) {
        if (e instanceof EurekaInstanceRegisteredEvent) {
            EurekaInstanceRegisteredEvent event = (EurekaInstanceRegisteredEvent) e;
            logger.warn("## [RegistryEvent] Register eureka instance. {}, {}", event.getInstanceInfo(),
                        event.getLeaseDuration());
        } else if (e instanceof EurekaInstanceRenewedEvent) {
            EurekaInstanceRenewedEvent event = (EurekaInstanceRenewedEvent) e;
            logger.warn("## [RegistryEvent] Renew eureka instance. appName: {}, serviceId: {}", event.getAppName(),
                        event.getServerId());
        } else if (e instanceof EurekaInstanceCanceledEvent) {
            EurekaInstanceCanceledEvent event = (EurekaInstanceCanceledEvent) e;
            logger.warn("## [RegistryEvent] Cancel eureka instance. appName: {}, serviceId: {}", event.getAppName(),
                        event.getServerId());
        } else {
            logger.warn("## Unknown event type: {}", e.getClass().getSimpleName());
        }
    }
}
