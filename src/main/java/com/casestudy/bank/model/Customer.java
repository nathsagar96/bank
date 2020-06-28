package com.casestudy.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    @NotNull(message = "First Name cannot be null")
    @Size(max = 65)
    private String firstName;

    @NotNull(message = "Last Name cannot be null")
    @Size(max = 65)
    private String lastName;

    @Email(message = "Invalid Email format")
    @NotNull(message = "Email cannot be null")
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    @JsonIgnoreProperties("customers")
    private Account account;

    public Customer(String firstName, String lastName, String email, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.account = account;
    }
}
