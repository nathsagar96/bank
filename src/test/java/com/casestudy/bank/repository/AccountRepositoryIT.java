package com.casestudy.bank.repository;

import com.casestudy.bank.model.Account;
import com.casestudy.bank.model.AccountType;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
public class AccountRepositoryIT {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AccountRepository accountRepository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DisplayName("Find ALL Customers")
    void testFindAll() {
        List<Account> accounts = Lists.newArrayList(accountRepository.findAll());
        Assertions.assertEquals(3, accounts.size(), "Expected 3 accounts in the database");
    }

    @Test
    @DisplayName("Find By Id Success")
    void testFindByIdSuccess() {
        Optional<Account> account = accountRepository.findById(1);
        Assertions.assertTrue(account.isPresent(), "We should find a account with ID 1");

        Account a = account.get();
        Assertions.assertEquals(1, a.getAccountId(), "The widget ID should be 1");
        Assertions.assertEquals(AccountType.SAVING, a.getAccountType(), "Incorrect Account Type");
        Assertions.assertEquals(20000.0, a.getBalance(), "Incorrect Balance");
    }

    @Test
    @DisplayName("Find by ID Not Found")
    void testFindByIdNotFound() {
        Optional<Account> account = accountRepository.findById(4);
        Assertions.assertFalse(account.isPresent(), "A account with ID 4 should not be found");
    }

    @Test
    @DisplayName("Find by AccountId Sucess")
    void testFindByAccountIdSuccess() {
        Account account = accountRepository.findByAccountId(1);
        Assertions.assertNotNull(account, "A account should be returned");
    }

    @Test
    @DisplayName("Find by AccountId Not Found")
    void testFindByAccountIdNotFound() {
        Account account = accountRepository.findByAccountId(4);
        Assertions.assertFalse(account != null, "A account should not be returned");
    }
}