package com.example.p2ptransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindByIdCardDataDto {

    private String cardHolder;
    private String message;
    private String phoneNumber;

}
