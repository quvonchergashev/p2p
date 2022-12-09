package com.example.p2ptransaction.service.interfaces;

import com.example.p2ptransaction.dto.EmailDto;
import com.example.p2ptransaction.dto.TransactionDto;
import com.example.p2ptransaction.dto.VerificationCodeDto;
import com.example.p2ptransaction.payload.ResponseApi;

import java.util.Map;

public interface TransactionService {
     Map<String, String> checkCard(Long cardId);
     Map<String, String> checkVerificationCode(VerificationCodeDto verificationCodeDto);

     EmailDto transactionEmailMessage(TransactionDto transactionDto);

     Map<String, String> findByToken(Long cardId);

     boolean checkPhoneNumber(Long cardId);

     ResponseApi transaction(VerificationCodeDto verificationCodeDto);








}
