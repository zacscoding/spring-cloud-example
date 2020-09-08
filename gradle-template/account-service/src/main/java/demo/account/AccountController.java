package demo.account;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResource>> getAccounts() {
        return ResponseEntity.ok(accountRepository.findAll()
                                                  .stream()
                                                  .map(AccountAssembler::toResource)
                                                  .collect(Collectors.toList()));
    }

    @PostMapping("/account")
    public ResponseEntity<AccountResource> saveAccount(AccountResource accountRequest) {
        final Account saved = accountRepository.save(AccountAssembler.toEntity(accountRequest));
        return ResponseEntity.ok(AccountAssembler.toResource(saved));
    }

    @GetMapping("/account/{email}")
    public ResponseEntity<AccountResource> getAccount(@PathVariable("email") String email) {
        try {
            final AccountResource resource = AccountAssembler.toResource(
                    accountRepository.findByEmail(email).orElseThrow(NotFoundException::new));
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
