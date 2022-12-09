package com.example.p2ptransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardMask;
    private String cardNumber;
    private String validityPeriod;
    private Long balance;
    private String pin;
    private LocalDateTime cardAddDate;
    private boolean isBlocked;
    private String cardType;
    private String connectionPhoneNumber;
    private String cardHolder;
}
