package com.example.p2ptransaction.dto;

import com.example.p2ptransaction.entity.EOPS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiveMoneyDto {
    private Long amountTransfer;
    private String cardToken;
    private EOPS eops;
}
