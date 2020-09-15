package demo.basic.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

/**
 * Tests service(remote) layer
 */
@ExtendWith(SpringExtension.class)
@RestClientTest({ AccountRemoteService.class })
@AutoConfigureWebClient(registerRestTemplate = true)
public class AccountRemoteServiceTest {

    @Value("${account-service.host:account-service}")
    String endpoint;
    @Autowired
    AccountRemoteService accountRemoteService;
    @Autowired
    MockRestServiceServer server;

    @Test
    public void testGetAuthenticatedAccount() {
        System.out.println(endpoint);
        // given
        server.expect(requestTo(String.format("%s/v1/account/me", endpoint)))
              .andRespond(withSuccess(new ClassPathResource("account.json", AccountRemoteServiceTest.class),
                                      MediaType.APPLICATION_JSON));

        // when
        final AccountProfile account = accountRemoteService.getAuthenticatedAccount();

        // then
        assertThat(account.getAccountId()).isEqualTo("5c6d4b35-a854-407f-815d-21cf7993e2fc");
        assertThat(account.getName()).isEqualTo("user1");
        assertThat(account.getBio()).isEqualTo("user1 bio");
    }
}