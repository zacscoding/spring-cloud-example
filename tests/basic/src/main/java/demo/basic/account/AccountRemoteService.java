package demo.basic.account;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountRemoteService {

    private final String endpoint;
    private final RestTemplate restTemplate;

    @Autowired
    public AccountRemoteService(RestTemplate restTemplate,
                                @Value("${account-service.host:account-service}") String endpoint) {

        logger.info("## initialize account service. host: {} / restTemplate : {}", endpoint,
                    restTemplate.getClass().getName());

        this.endpoint = endpoint;
        this.restTemplate = restTemplate;
    }

    /**
     * Call {endpoint}/v1/account/me to get current authenticated account
     */
    public AccountProfile getAuthenticatedAccount() {
        final URI uri = UriComponentsBuilder.fromHttpUrl(endpoint)
                                            .pathSegment("v1", "account", "me")
                                            .build()
                                            .toUri();
        return restTemplate.getForObject(uri, AccountProfile.class);
    }

    /**
     * Call {endpoint}/v1/account/profile/{accountId} to get given account's profile
     */
    public AccountProfile getAccountProfileById(String accountId) {
        final URI uri = UriComponentsBuilder.fromHttpUrl(endpoint)
                                            .pathSegment("v1", "account", "profile", accountId)
                                            .build()
                                            .toUri();
        return restTemplate.getForObject(uri, AccountProfile.class);
    }
}
