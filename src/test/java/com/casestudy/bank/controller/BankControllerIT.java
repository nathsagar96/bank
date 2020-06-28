package com.casestudy.bank.controller;

import com.casestudy.bank.BankApplication;
import com.casestudy.bank.dto.FundTransfer;
import com.casestudy.bank.model.AccountType;
import com.casestudy.bank.model.Customer;
import com.casestudy.bank.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;

import static com.casestudy.bank.controller.BankControllerTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BankControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BankService bankService;

    @Autowired
    ObjectMapper objectMapper;

    public static final MediaType APPLICATION_TEXT_UTF8 = new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8")
    );

    @Test
    @DisplayName("GET /customersS")
    void testGetALLCustomer() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/bank/customers"))
                //Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //validate the returned fields
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Sagar")))
                .andExpect(jsonPath("$[0].lastName", is("Nath")))
                .andExpect(jsonPath("$[0].email", is("sagarnath@example.com")))
                .andExpect(jsonPath("$[0].account.accountId", is(1)))
                .andExpect(jsonPath("$[0].account.accountType", is(AccountType.SAVING.toString())))
                .andExpect(jsonPath("$[0].account.balance", is(20000.0))).andReturn();
    }

    @Test
    @DisplayName("GET /customer/1 ")
    void testGetOneCustomer() throws Exception {
        //Execute the GET Request
        MvcResult mvcResult = mockMvc.perform(get("/bank/customer/{customerId}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //validate returned fields
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Sagar")))
                .andExpect(jsonPath("$.lastName", is("Nath")))
                .andExpect(jsonPath("$.email", is("sagarnath@example.com")))
                .andExpect(jsonPath("$.account.accountId", is(1)))
                .andExpect(jsonPath("$.account.accountType", is(AccountType.SAVING.toString())))
                .andExpect(jsonPath("$.account.balance", is(20000.0))).andReturn();
        Customer customer = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Customer.class);
        Assertions.assertNotNull(customer, "Customer should be returned");
    }

    @Test
    @DisplayName("PUT /bank/customer/3")
    void testUpdateCustomer() throws Exception {
        // Setup PUT data
        Customer customerToPut = new Customer("Pooja", "Nath", "bahavananath@example.com", null);

        // Execute the PUT request
        MvcResult mvcResult = mockMvc.perform(put("/bank/customer/{customerId}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerToPut)))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.customerId", is(3)))
                .andExpect(jsonPath("$.firstName", is("Pooja")))
                .andExpect(jsonPath("$.lastName", is("Nath")))
                .andExpect(jsonPath("$.email", is("bahavananath@example.com"))).andReturn();

        Customer customer = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Customer.class);
        Assertions.assertNotNull(customer, "Customer details should be updated");
        Assertions.assertEquals("Pooja", customer.getFirstName(), "First Name should be updated");
    }

    @Test
    @DisplayName("GET /bank/balance/1 ")
    void testGetBalanceOf() throws Exception {
        //Execute the GET Request
        MvcResult mvcResult = mockMvc.perform(get("/bank/balance/{accountId}", 1))
                //validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //validate returned fields
                .andExpect(jsonPath("$.accountId", is(1)))
                .andExpect(jsonPath("$.accountType", is(AccountType.SAVING.toString())))
                .andExpect(jsonPath("$.balance", is(20000.0))).andReturn();
        int httpStatus = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, httpStatus, "Account Should be returned");
    }

    @Test
    @DisplayName("POST /bank/transfer - SUCCESS")
    void testTransferFundSuccess() throws Exception {
        //Setup POST data
        FundTransfer fundTransfer = new FundTransfer(3, 2, 200.0);

        //Execute the POST request
        MvcResult mvcResult = mockMvc.perform(post("/bank/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(fundTransfer)))
                //Validate the response code and content
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_TEXT_UTF8))
                //validate the result
                .andExpect(content().string("SUCCESS")).andReturn();
        String status = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("SUCCESS", status, "The Transfer should happen");
    }
}

