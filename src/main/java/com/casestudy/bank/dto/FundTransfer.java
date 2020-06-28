package com.casestudy.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundTransfer implements Serializable {

    private Integer fromAccount;

    private Integer toAccount;

    private Double amount;

}
