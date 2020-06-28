package com.casestudy.bank.service;

import com.casestudy.bank.model.Account;
import com.casestudy.bank.model.Customer;
import com.casestudy.bank.repository.AccountRepository;
import com.casestudy.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    //create new account
    public Account addAccount(Account request) {
        String accountType = request.getAccountType().toString();
        if (accountType.equals("SAVING") || accountType.equals("CURRENT")) {
            if (request.getCustomers().size() > 1)
                return null;
            else
                saveAccountDetails(request);
        } else {
            if (request.getCustomers().size() < 2)
                return null;
            else
                saveAccountDetails(request);
        }
        return accountRepository.save(request);
    }

    //Save Customer Details
    public void saveAccountDetails(Account request) {
        List<Customer> customerList = request.getCustomers();
        List<Customer> newCustomerList = new ArrayList<>();
        for (Customer customer : customerList) {
            Customer newCustomer = new Customer();
            newCustomer.setFirstName(customer.getFirstName());
            newCustomer.setLastName(customer.getLastName());
            newCustomer.setEmail(customer.getEmail().toLowerCase());
            newCustomerList.add(newCustomer);
        }
        for (Customer customer : newCustomerList) {
            customer.setAccount(request);
        }
        request.setCustomers(newCustomerList);
    }

    //update customer details
    public Customer upDateCustomer(Integer customerId, Customer request) {
        Customer customer = customerRepository.findById(customerId).get();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        try {
            customer = customerRepository.save(customer);
        } catch (Exception exception) {
            customer = null;
        }
        return customer;
    }

    //get all customer details
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    // get one customer details
    public Optional<Customer> getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId);
    }

    //Transfer fund
    public String transferFunds(Integer from, Integer to, Double amount) {
        Account fromAccount = accountRepository.findByAccountId(from);
        Account toAccount = accountRepository.findByAccountId(to);

        if (fromAccount != null && toAccount != null) {
            if (amount <= fromAccount.getBalance()) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);
                return "SUCCESS";
            } else {
                return "INSUFFICIENT FUNDS";
            }
        } else
            return "ID MISMATCH";
    }

    //Get Balance Details
    public Optional<Account> getBalanceOf(Integer accountId) {
        return accountRepository.findById(accountId);
    }

}