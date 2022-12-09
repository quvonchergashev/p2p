package com.example.p2ptransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCardDto {
    private String cardNumber;
    private String validityPeriod;
    private String cardName;
}
