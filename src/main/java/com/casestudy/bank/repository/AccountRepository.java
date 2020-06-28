package com.casestudy.bank.repository;

import com.casestudy.bank.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    Account findByAccountId(Integer accountId);
}
