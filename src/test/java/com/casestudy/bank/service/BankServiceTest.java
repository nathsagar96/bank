package com.casestudy.bank.service;

import com.casestudy.bank.model.Account;
import com.casestudy.bank.model.AccountType;
import com.casestudy.bank.model.Customer;
import com.casestudy.bank.repository.AccountRepository;
import com.casestudy.bank.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class BankServiceTest {

    @Autowired
    BankService bankService;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    CustomerRepository customerRepository;

    @Test
    @DisplayName("Test getCustomerByID Success")
    void testGetCustomerByIdSuccess() {
        //Setup our mock repository
        Account account = new Account(1, AccountType.SAVING, 200.0, null);
        Customer customer = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", account);
        doReturn(Optional.of(customer)).when(customerRepository).findById(1);

        //Execute the service call
        Optional<Customer> returnedCustomer = bankService.getCustomerById(1);

        //Assert the response
        Assertions.assertTrue(returnedCustomer.isPresent(), "Customer was not found");
        Assertions.assertSame(returnedCustomer.get(), customer, "The Customer was not the same as the mock");
    }

    @Test
    @DisplayName("Test getCustomerById Not Found")
    void testGetCustomerByIdNotFound() {
        //Setup our mock repository
        doReturn(Optional.empty()).when(customerRepository).findById(1);

        //Execute the service call
        Optional<Customer> returnedCustomer = bankService.getCustomerById(1);

        //Assert the response
        Assertions.assertFalse(returnedCustomer.isPresent(), "Customer should not be found");
    }

    @Test
    @DisplayName("Test getAllCustomers")
    void testGetAllCustomer() {
        //Setup our mock repository
        Customer customer1 = new Customer(1, "Bhavana", "Nath", "bhavana.nath@example.com", null);
        Customer customer2 = new Customer(2, "Sagar", "Nath", "sagar.nath@example.com", null);
        doReturn(Arrays.asList(customer1, customer2)).when(customerRepository).findAll();

        //Execute the service call
        List<Customer> customers = bankService.getAllCustomers();

        //Assert the response
        Assertions.assertEquals(2, customers.size(), "getALLCustomers should return 2 customers");
    }

    @Test
    @DisplayName("test addAccount SAVING with one customer detail")
    void testAddAccountSavingWithOneCustomer() {
        //Setup our mock repository
        Customer customer = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Account account = new Account(1, AccountType.SAVING, 200.0, Arrays.asList(customer));
        doReturn(account).when(accountRepository).save(any());

        //Execute the service call
        Account returnedAccount = bankService.addAccount(account);

        //Assert the response
        Assertions.assertNotNull(returnedAccount, "The Account should not be null");
        Assertions.assertEquals(AccountType.SAVING, returnedAccount.getAccountType(), "The AccountType should be SAVING");
    }

    @Test
    @DisplayName("test addAccount SAVING with more than customer detail")
    void testAddAccountSavingWithTwoCustomer() {
        //Setup our mock repository
        Customer customer1 = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Customer customer2 = new Customer(2, "Deepak", "Nath", "deeapk.nath@example.com", null);
        Account account = new Account(1, AccountType.SAVING, 200.0, Arrays.asList(customer1, customer2));
        doReturn(null).when(accountRepository).save(any());

        //Execute the service call
        Account returnedAccount = bankService.addAccount(account);

        //Assert the response
        Assertions.assertFalse(returnedAccount != null, "Account Should not be created");
    }

    @Test
    @DisplayName("test addAccount CURRENT with one customer detail")
    void testAddAccountCurrentWithOneCustomer() {
        //Setup our mock repository
        Customer customer = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Account account = new Account(1, AccountType.CURRENT, 200.0, Arrays.asList(customer));
        doReturn(account).when(accountRepository).save(account);

        //Execute the service call
        Account returnedAccount = bankService.addAccount(account);

        //Assert the response
        Assertions.assertNotNull(returnedAccount, "The Account should not be null");
        Assertions.assertEquals(AccountType.CURRENT, returnedAccount.getAccountType(), "The AccountType should be CURRENT");
    }

    @Test
    @DisplayName("test addAccount CURRENT with more than one customer detail")
    void testAddAccountCurrentWithTwoCustomer() {
        //Setup our mock repository
        Customer customer1 = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Customer customer2 = new Customer(2, "Deepak", "Nath", "deeapk.nath@example.com", null);
        Account account = new Account(1, AccountType.CURRENT, 200.0, Arrays.asList(customer1, customer2));
        doReturn(null).when(accountRepository).save(any());

        //Execute the service call
        Account returnedAccount = bankService.addAccount(account);

        //Assert the response
        Assertions.assertFalse(returnedAccount != null, "Account Should not be created");

    }

    @Test
    @DisplayName("test addAccount JOINT with more than 2 customer detail")
    void testAddAccountJointWithManyCustomer() {
        //Setup our mock repository
        Customer customer1 = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Customer customer2 = new Customer(2, "Deepak", "Nath", "deepak.nath@example.com", null);
        Account account = new Account(1, AccountType.JOINT, 200.0, Arrays.asList(customer1, customer2));
        doReturn(account).when(accountRepository).save(account);

        //Execute the service call
        Account returnedAccount = bankService.addAccount(account);
        //Assert the response
        Assertions.assertNotNull(returnedAccount, "The account should not be null");
        Assertions.assertEquals(AccountType.JOINT, returnedAccount.getAccountType(), "The AccountType should be JOINT");
        Assertions.assertEquals(2, returnedAccount.getCustomers().size(), "The count of customers should be 2");
    }

    @Test
    @DisplayName("test addAccount JOINT with one customer detail")
    void testAddAccountJointWithOneCustomer() {
        //Setup our mock repository
        Customer customer1 = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Account account = new Account(1, AccountType.JOINT, 200.0, Arrays.asList(customer1));
        doReturn(null).when(accountRepository).save(any());

        //Execute the service call
        Account returnedAccount = bankService.addAccount(account);

        //Assert the response
        Assertions.assertFalse(returnedAccount != null, "Account Should not be created");
    }

    @Test
    @DisplayName("test upDateCustomer with unique updated email")
    void testUpdateCustomerSuccess() {
        //Setup our mock repository
        Customer customer1 = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Customer customer2 = new Customer(1, "Deepak", "Nath", "deepak.nath@example.com", null);
        doReturn(Optional.of(customer1)).when(customerRepository).findById(1);
        doReturn(customer2).when(customerRepository).save(any());

        //Execute the service call
        Customer returnedCustomer = bankService.upDateCustomer(1, customer1);

        //Assert the response
        Assertions.assertNotNull(returnedCustomer, "Customer details not updated");
        Assertions.assertEquals("Deepak", returnedCustomer.getFirstName(), "Customer Details were not updated");
    }

    @Test
    @DisplayName("test upDateCustomer with existing customer having same email")
    void testUpdateCustomerFail() {
        //Setup our mock repository
        Customer customer1 = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Customer customer2 = new Customer(1, "Deepak", "Nath", "deepak.nath@example.com", null);
        doReturn(Optional.of(customer1)).when(customerRepository).findById(1);
        doReturn(null).when(customerRepository).save(any());

        //Execute the service call
        Customer returnedCustomer = bankService.upDateCustomer(1, customer1);

        //Assert the response
        Assertions.assertFalse(returnedCustomer != null, "Customer Details Should not be Updated");
    }

    @Test
    @DisplayName("test transferFunds with valid account and sufficient funds")
    void testTransferFundsValidAccountSufficientFund() {
        //Setup our mock repository
        Account account1 = new Account(1, AccountType.JOINT, 500.0, null);
        Account account2 = new Account(2, AccountType.CURRENT, 200.0, null);
        doReturn(account1).when(accountRepository).findByAccountId(1);
        doReturn(account2).when(accountRepository).findByAccountId(2);

        //Execute the service call
        String status = bankService.transferFunds(1, 2, 300.0);
        //Assert the response
        Assertions.assertEquals("SUCCESS", status, "Transfer should happen");
    }

    @Test
    @DisplayName("test transferFunds with valid account and insufficient funds")
    void testTransferFundsValidAccountInSufficientFund() {
        //Setup our mock repository
        Account account1 = new Account(1, AccountType.JOINT, 500.0, null);
        Account account2 = new Account(2, AccountType.CURRENT, 200.0, null);
        doReturn(account1).when(accountRepository).findByAccountId(1);
        doReturn(account2).when(accountRepository).findByAccountId(2);

        //Execute the service call
        String status = bankService.transferFunds(1, 2, 600.0);
        //Assert the response
        Assertions.assertEquals("INSUFFICIENT FUNDS", status, "Transfer should not happen");
    }

    @Test
    @DisplayName("test transferFunds with invalid account")
    void testTransferFundsInValidAccount() {
        //Setup our mock repository
        Account account1 = new Account(1, AccountType.JOINT, 500.0, null);
        doReturn(account1).when(accountRepository).findByAccountId(1);
        doReturn(null).when(accountRepository).findByAccountId(2);

        //Execute the service call
        String status = bankService.transferFunds(1, 2, 200.0);
        //Assert the response
        Assertions.assertEquals("ID MISMATCH", status, "Transfer should not happen");
    }

    @Test
    @DisplayName("Test getBalanceInfo Success")
    void testGetBalanceInfoSuccess() {
        //Setup our mock repository
        Customer customer = new Customer(1, "Sagar", "Nath", "sagar.nath@example.com", null);
        Account account = new Account(1, AccountType.SAVING, 200.0, Arrays.asList(customer));
        doReturn(Optional.of(account)).when(accountRepository).findById(1);

        //Execute the service call
        Optional<Account> returnedAccount = bankService.getBalanceOf(1);


        //Assert the response
        Assertions.assertTrue(returnedAccount.isPresent(), "Account was not found");
        Assertions.assertSame(returnedAccount.get(), account, "The Account was not the same as the mock");
    }

    @Test
    @DisplayName("Test getBalanceInfo Failure")
    void testGetBalanceInfoNotFound() {
        //Setup our mock repository
        doReturn(Optional.empty()).when(accountRepository).findById(1);

        //Execute the service call
        Optional<Account> returnedAccount = bankService.getBalanceOf(1);

        //Assert the response
        Assertions.assertFalse(returnedAccount.isPresent(), "Account should not be found");
    }
}