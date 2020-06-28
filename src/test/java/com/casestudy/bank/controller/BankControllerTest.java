package com.casestudy.bank.controller;

import com.casestudy.bank.dto.FundTransfer;
import com.casestudy.bank.model.Account;
import com.casestudy.bank.model.AccountType;
import com.casestudy.bank.model.Customer;
import com.casestudy.bank.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest {

    @MockBean
    BankService bankService;

    @Autowired
    private MockMvc mockMvc;

    public static final MediaType APPLICATION_TEXT_UTF8 = new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8")
    );

    @Test
    @DisplayName("GET /customers success")
    void testGetALLCustomer() throws Exception {
        //setup our mocked service
        Account account = new Account(1, AccountType.SAVING, 200.0, null);
        Customer customer1 = new Customer(1, "Bhavana", "Nath", "bhavana.nath@example.com", account);
        Customer customer2 = new Customer(2, "Sagar", "Nath", "sagar.nath@example.com", account);
        doReturn(Arrays.asList(customer1, customer2)).when(bankService).getAllCustomers();

        //Execute the GET request
        mockMvc.perform(get("/bank/customers"))
                //Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Bhavana")))
                .andExpect(jsonPath("$[0].lastName", is("Nath")))
                .andExpect(jsonPath("$[0].email", is("bhavana.nath@example.com")))
                .andExpect(jsonPath("$[0].account.accountId", is(1)))
                .andExpect(jsonPath("$[0].account.accountType", is(AccountType.SAVING.toString())))
                .andExpect(jsonPath("$[0].account.balance", is(200.0)))
                .andExpect(jsonPath("$[1].customerId", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Sagar")))
                .andExpect(jsonPath("$[1].lastName", is("Nath")))
                .andExpect(jsonPath("$[1].email", is("sagar.nath@example.com")))
                .andExpect(jsonPath("$[1].account.accountId", is(1)))
                .andExpect(jsonPath("$[1].account.accountType", is(AccountType.SAVING.toString())))
                .andExpect(jsonPath("$[1].account.balance", is(200.0)));
        verify(bankService).getAllCustomers();
    }

    @Test
    @DisplayName("GET /customer/1 ")
    void testGetOneCustomer() throws Exception {
        //setup our mocked service
        Account account = new Account(1, AccountType.SAVING, 200.0, null);
        Customer customer = new Customer(1, "Bhavana", "Nath", "bhavana.nath@example.com", account);
        doReturn((Optional.of(customer))).when(bankService).getCustomerById(1);

        //Execute the GET Request
        mockMvc.perform(get("/bank/customer/{customerId}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //validate returned fields
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Bhavana")))
                .andExpect(jsonPath("$.lastName", is("Nath")))
                .andExpect(jsonPath("$.email", is("bhavana.nath@example.com")))
                .andExpect(jsonPath("$.account.accountId", is(1)))
                .andExpect(jsonPath("$.account.accountType", is(AccountType.SAVING.toString())))
                .andExpect(jsonPath("$.account.balance", is(200.0)));
        verify(bankService).getCustomerById(1);
    }

    @Test
    @DisplayName("GET /bank/customer/1 - Not Found")
    void testGetOneCustomerNotFound() throws Exception {
        // Setup our mocked service
        doReturn(Optional.empty()).when(bankService).getCustomerById(1);

        // Execute the GET request
        mockMvc.perform(get("/bank/customer/{customerId}", 1))
                // Validate the response code
                .andExpect(status().isNotFound());
        verify(bankService).getCustomerById(1);
    }

    @Test
    @DisplayName("POST /bank/account - SUCCESS")
    void testAddAccount() throws Exception {
        // Setup our mocked service
        Customer customer1 = new Customer(1, "Bhavana", "Nath", "bhavana.nath@example.com", null);
        Customer customer2 = new Customer(2, "Sagar", "Nath", "sagar.nath@example.com", null);
        Account accountToPost = new Account(1, AccountType.JOINT, 200.0, Arrays.asList(customer1, customer2));
        Account accountToReturn = new Account(1, AccountType.JOINT, 200.0, Arrays.asList(customer1, customer2));
        doReturn(accountToReturn).when(bankService).addAccount(any());

        // Execute the POST request
        mockMvc.perform(post("/bank/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountToPost)))
                // Validate the response code and content type
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.accountId", is(1)))
                .andExpect(jsonPath("$.accountType", is(AccountType.JOINT.toString())))
                .andExpect(jsonPath("$.balance", is(200.0)))
                .andExpect(jsonPath("$.customers[0].customerId", is(1)))
                .andExpect(jsonPath("$.customers[0].firstName", is("Bhavana")))
                .andExpect(jsonPath("$.customers[0].lastName", is("Nath")))
                .andExpect(jsonPath("$.customers[0].email", is("bhavana.nath@example.com")))
                .andExpect(jsonPath("$.customers[1].customerId", is(2)))
                .andExpect(jsonPath("$.customers[1].firstName", is("Sagar")))
                .andExpect(jsonPath("$.customers[1].lastName", is("Nath")))
                .andExpect(jsonPath("$.customers[1].email", is("sagar.nath@example.com")));
        verify(bankService).addAccount(any());
    }

    @Test
    @DisplayName("POST /bank/account - FAILURE")
    void testAddAccountFailure() throws Exception {
        // Setup our mocked service
        Customer customer1 = new Customer(1, "Bhavana", "Nath", "bhavana.nath@example.com", null);
        Customer customer2 = new Customer(2, "Sagar", "Nath", "sagar.nath@example.com", null);
        Account accountToPost = new Account(1, AccountType.SAVING, 200.0, Arrays.asList(customer1, customer2));
        doReturn(null).when(bankService).addAccount(any());

        // Execute the POST request
        mockMvc.perform(post("/bank/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountToPost)))
                // Validate the response code and content type
                .andExpect(status().isBadRequest());
        verify(bankService).addAccount(any());
    }

    @Test
    @DisplayName("PUT /bank/customer/1 - SUCCESS")
    void testUpdateCustomerSuccess() throws Exception {
        // Setup our mocked service
        Customer customerToPut = new Customer("Bhavana", "Nath", "bhavana.nath@example.com", null);
        Customer customerReturnFindBy = new Customer(1, "Sagar", "Nath", "bhavana.nath@example.com", null);
        Customer customerReturnSave = new Customer(1, "Bhavana", "Nath", "bhavana.nath@example.com", null);
        doReturn(Optional.of(customerReturnFindBy)).when(bankService).getCustomerById(1);
        doReturn(customerReturnSave).when(bankService).upDateCustomer(1, customerToPut);

        // Execute the PUT request
        mockMvc.perform(put("/bank/customer/{customerId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerToPut)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Bhavana")))
                .andExpect(jsonPath("$.lastName", is("Nath")))
                .andExpect(jsonPath("$.email", is("bhavana.nath@example.com")));
        verify(bankService).upDateCustomer(1, customerToPut);
    }

    @Test
    @DisplayName("PUT /bank/customer/1 - Conflict")
    void testUpdateCustomerConflict() throws Exception {
        // Setup our mocked service
        Customer customerToPut = new Customer("Bhavana", "Nath", "bhavana.nath@example.com", null);
        doReturn(Optional.of(customerToPut)).when(bankService).getCustomerById(1);
        doReturn(null).when(bankService).upDateCustomer(1, customerToPut);

        // Execute the POST request
        mockMvc.perform(put("/bank/customer/{customerId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerToPut)))
                // Validate the response code and content type
                .andExpect(status().isConflict());
        verify(bankService).upDateCustomer(1, customerToPut);
    }

    @Test
    @DisplayName("PUT /bank/customer/1 - Not Found")
    void testUpdateCustomerNotFound() throws Exception {
        // Setup our mocked service
        Customer customerToPut = new Customer("Bhavana", "Nath", "bhavana.nath@example.com", null);
        doReturn(Optional.empty()).when(bankService).getCustomerById(1);

        // Execute the POST request
        mockMvc.perform(put("/bank/customer/{customerId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerToPut)))
                // Validate the response code and content type
                .andExpect(status().isNotFound());
        verify(bankService).getCustomerById(1);
    }

    @Test
    @DisplayName("GET /bank/balance/1 ")
    void testGetBalanceOf() throws Exception {
        //setup our mocked service
        Account account = new Account(1, AccountType.SAVING, 200.0, null);
        doReturn((Optional.of(account))).when(bankService).getBalanceOf(1);

        //Execute the GET Request
        mockMvc.perform(get("/bank/balance/{accountId}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //validate returned fields
                .andExpect(jsonPath("$.accountId", is(1)))
                .andExpect(jsonPath("$.accountType", is(AccountType.SAVING.toString())))
                .andExpect(jsonPath("$.balance", is(200.0)));
        verify(bankService).getBalanceOf(1);
    }

    @Test
    @DisplayName("GET /bank/balance/3 - Not Found")
    void testGetBalanceOfNotFound() throws Exception {
        // Setup our mocked service
        doReturn(Optional.empty()).when(bankService).getBalanceOf(anyInt());

        // Execute the GET request
        mockMvc.perform(get("/bank/customer/{customerId}", 1))
                // Validate the response code
                .andExpect(status().isNotFound());
        verify(bankService).getCustomerById(anyInt());
    }

    @Test
    @DisplayName("POST /bank/transfer - SUCCESS")
    void testTransferFundSuccess() throws Exception {
        //setup our mocked service
        String message = "SUCCESS";
        FundTransfer fundTransfer = new FundTransfer(1, 2, 200.0);
        doReturn(message).when(bankService).transferFunds(fundTransfer.getFromAccount(), fundTransfer.getToAccount(), fundTransfer.getAmount());

        //Execute the POST request
        mockMvc.perform(post("/bank/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fundTransfer)))
                //Validate the response code and content
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_TEXT_UTF8))
                //validate the result
                .andExpect(content().string("SUCCESS"));
        verify(bankService).transferFunds(anyInt(), anyInt(), anyDouble());
    }

    @Test
    @DisplayName("POST /bank/transfer - Not Found")
    void testTransferFundNotFound() throws Exception {
        //setup our mocked service
        String message = "ID MISMATCH";
        FundTransfer fundTransfer = new FundTransfer(1, 2, 200.0);
        doReturn(message).when(bankService).transferFunds(fundTransfer.getFromAccount(), fundTransfer.getToAccount(), fundTransfer.getAmount());

        //Execute the POST request
        mockMvc.perform(post("/bank/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fundTransfer)))
                //Validate the response code and content
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_TEXT_UTF8))
                //validate the result
                .andExpect(content().string("ID MISMATCH"));
        verify(bankService).transferFunds(anyInt(), anyInt(), anyDouble());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}