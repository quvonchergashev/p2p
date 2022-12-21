package com.example.p2ptransaction.controller;

import com.example.p2ptransaction.dto.*;
import com.example.p2ptransaction.entity.Card;
import com.example.p2ptransaction.payload.ResponseApi;
import com.example.p2ptransaction.service.interfaces.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/find-by-card-number/{cardNumber}")
    public ResponseEntity<?> findByCardNumber(
            @PathVariable String cardNumber
    ) {
        FindByIdCardDataDto byCard = cardService.findByCard(cardNumber);
        return ResponseEntity.ok(byCard);
    }
    @GetMapping("/add-card-send-email")
    public ResponseEntity<?> addCardSendEmail(
            @RequestBody AddCardDto addCardDto
    ) {
        EmailDto emailDto = cardService.addCardEmailMessage(addCardDto);
        return ResponseEntity.ok(emailDto);
    }
    @GetMapping("/add-card")
    public ResponseEntity<?> addCard(
            @RequestBody VerificationCodeDto verificationCodeDto
    ){
        ResponseApi responseApi = cardService.addCard(verificationCodeDto);
        if (responseApi.isSuccess()) return ResponseEntity.ok(responseApi);
        return ResponseEntity.status(409).body(responseApi);
    }
    @GetMapping("/find-all-by-client-id/{userId}")
    public ResponseEntity<?> findAllByClientId(
            @PathVariable Long userId
    ){
        List<Card> allByClientId = cardService.findAllByClientId(userId);
        return ResponseEntity.ok(allByClientId);
    }
}
