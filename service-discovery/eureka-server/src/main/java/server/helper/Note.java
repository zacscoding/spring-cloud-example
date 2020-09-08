package server.helper;

import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.InstanceRegistry;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.eureka.registry.AbstractInstanceRegistry;
import com.netflix.eureka.registry.PeerAwareInstanceRegistryImpl;
import com.netflix.eureka.resources.ApplicationResource;
import com.netflix.eureka.resources.PeerReplicationResource;

/**
 * For note classes
 *
 * - Spring registry {@link InstanceRegistry}
 * - Eureka Server
 *   - Handler {@link ApplicationResource}, {@link PeerReplicationResource}
 *   - Peer aware {@link PeerAwareInstanceRegistryImpl}
 *   - Registry {@link AbstractInstanceRegistry}
 * - Eureka Client
 *   - Spring cloud {@link EurekaDiscoveryClient}
 *   - Eureka {@link DiscoveryClient},
 */
public class Note {
}
