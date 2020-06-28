package com.casestudy.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @NotNull(message = "AccountType cannot be null")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @NotNull(message = "Account Balance cannot be null")
    private Double balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnoreProperties("account")
    @NotNull(message = "Customer cannot be null")
    private List<Customer> customers;

    public Account(AccountType accountType, double balance, List<Customer> customers) {
        this.accountType = accountType;
        this.balance = balance;
        this.customers = customers;
    }
}
