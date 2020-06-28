package com.casestudy.bank.service;

import com.casestudy.bank.model.Account;
import com.casestudy.bank.model.Customer;
import com.casestudy.bank.repository.AccountRepository;
import com.casestudy.bank.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BankServiceIT {
    @Autowired
    BankService bankService;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @DisplayName("Get All Customer")
    void testGetALlCustomer() {
        //execute service call
        List<Customer> customerList = bankService.getAllCustomers();
        //Assert the reponse
        Assertions.assertEquals(4, customerList.size(), "4 Customers should be returned");
    }

    @Test
    @DisplayName("Test getCustomerByID")
    void testGetCustomerById() {
        //Execute the service call
        Optional<Customer> returnedCustomer = bankService.getCustomerById(1);

        //Assert the response
        Assertions.assertTrue(returnedCustomer.isPresent(), "Customer was not found");
    }

    @Test
    @DisplayName("test transferFunds")
    void testTransferFunds() {
        //Execute the service call
        String status = bankService.transferFunds(1, 2, 600.0);
        //Assert the response
        Assertions.assertEquals("SUCCESS", status, "Transfer should not happen");
    }

    @Test
    @DisplayName("Test getBalanceInfo Success")
    void testGetBalanceInfo() {
        //Execute the service call
        Optional<Account> returnedAccount = bankService.getBalanceOf(1);

        //Assert the response
        Assertions.assertTrue(returnedAccount.isPresent(), "Account was not found");
    }

}
