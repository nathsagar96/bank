package com.casestudy.bank.controller;

import com.casestudy.bank.dto.FundTransfer;
import com.casestudy.bank.model.Account;
import com.casestudy.bank.model.Customer;
import com.casestudy.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/account")
    public ResponseEntity<Account> addAccount(@Valid @RequestBody Account accountRequest) {
        try {
            Account account = bankService.addAccount(accountRequest);
            if (account != null)
                return new ResponseEntity<Account>(account, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/customer/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "customerId") Integer customerId, @Valid @RequestBody Customer customerRequest) {
        Optional<Customer> customer = bankService.getCustomerById(customerId);
        if (customer.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            Customer updatedCustomer = bankService.upDateCustomer(customerId, customerRequest);
            if (updatedCustomer != null)
                return ResponseEntity.ok(updatedCustomer);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Customer> getOneCustomer(@PathVariable(value = "customerId") Integer customerId) {
        Optional<Customer> customer = bankService.getCustomerById(customerId);
        if (!customer.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            return ResponseEntity.ok().body(customer.get());
        }
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getALLCustomers() {
        return new ResponseEntity<>(bankService.getAllCustomers(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFund(@Valid @RequestBody FundTransfer fundTransfer) {
        String result = bankService.transferFunds(fundTransfer.getFromAccount(), fundTransfer.getToAccount(), fundTransfer.getAmount());
        if (result.equals("SUCCESS")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result.equals("INSUFFICIENT FUNDS")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<Account> getBalanceOf(@PathVariable(value = "accountId") Integer accountId) {
        Optional<Account> account = bankService.getBalanceOf(accountId);
        if (account.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return ResponseEntity.ok().body(account.get());
    }
}
