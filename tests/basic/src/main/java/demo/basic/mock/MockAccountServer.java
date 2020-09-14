package demo.basic.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Mock account controller to demo application
 */
@Slf4j
@RestController
public class MockAccountServer {

    private final List<MockAccountProfile> accounts = new ArrayList<>();

    @PostConstruct
    private void setUp() {
        IntStream.rangeClosed(1, 5).forEach(i -> accounts.add(MockAccountProfile.builder()
                                                                                .accountId(UUID.randomUUID().toString())
                                                                                .name("user" + i)
                                                                                .bio("user bio" + i)
                                                                                .build()));
    }

    @GetMapping("/v1/account/me")
    public MockAccountProfile getCurrentAccount() {
        return accounts.get(new Random().nextInt(accounts.size()));
    }

    @GetMapping("/v1/account/profile/{accountId}")
    public ResponseEntity<MockAccountProfile> getAccount(@PathVariable("accountId") String accountId) {
        for (MockAccountProfile account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return ResponseEntity.ok(account);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class MockAccountProfile {

        private String accountId;

        private String name;

        private String bio;
    }
}
