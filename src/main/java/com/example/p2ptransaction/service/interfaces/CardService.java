package com.example.p2ptransaction.service.interfaces;

import com.example.p2ptransaction.dto.*;
import com.example.p2ptransaction.entity.Card;
import com.example.p2ptransaction.payload.ResponseApi;

import java.util.List;
import java.util.Optional;

public interface CardService {
    FindByIdCardDataDto findByCard(String cardNumber);
    EmailDto addCardEmailMessage(AddCardDto addCardDto);
    ResponseApi addCard(VerificationCodeDto verificationCodeDto);
    Optional<Card> findById(Long id);
    Card findByToken(String token);
    List<Card> findAllByClientId(Long userId);

}
