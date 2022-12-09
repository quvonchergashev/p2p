package com.example.p2ptransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String giveMoneyCardNumber;
    private Long getMoneyCardId;
    private Long amountTransfer;
}
    