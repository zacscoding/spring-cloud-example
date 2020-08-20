package demo.client;

import java.util.Set;

import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * See {@link RefreshEndpoint}
 */
@Slf4j
@AllArgsConstructor
@RestController
public class RefreshController {

    private final ContextRefresher contextRefresher;

    @PostMapping("/refresh")
    public Set<String> refresh() {
        logger.warn("## custom /refresh is called");
        return contextRefresher.refresh();
    }
}
