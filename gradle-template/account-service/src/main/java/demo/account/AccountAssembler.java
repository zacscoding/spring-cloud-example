package demo.account;

import com.google.common.base.Preconditions;

public final class AccountAssembler {

    public static AccountResource toResource(Account accountEntity) {
        Preconditions.checkNotNull(accountEntity, "accountEntity");

        return AccountResource.builder()
                              .id(accountEntity.getId())
                              .password(accountEntity.getPassword())
                              .email(accountEntity.getEmail())
                              .createdAt(accountEntity.getCreatedAt())
                              .updatedAt(accountEntity.getUpdatedAt())
                              .build();
    }

    public static Account toEntity(AccountResource accountDto) {
        Preconditions.checkNotNull(accountDto, "accountDto");

        return Account.createAccount(accountDto.getEmail(),
                                     accountDto.getPassword());
    }

    private AccountAssembler() {
    }
}