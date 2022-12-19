package com.example.p2ptransaction.controller;

import com.example.p2ptransaction.dto.EmailDto;
import com.example.p2ptransaction.dto.TransactionDto;
import com.example.p2ptransaction.dto.VerificationCodeDto;
import com.example.p2ptransaction.payload.ResponseApi;
import com.example.p2ptransaction.service.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/transaction-send-email")
    public ResponseEntity<?> transactionSendEmail(@RequestBody TransactionDto transactionDto) {
        EmailDto emailDto = transactionService.transactionEmailMessage(transactionDto);
        return ResponseEntity.ok(emailDto);
    }
    @GetMapping("/transaction")
    public ResponseEntity<?> transaction(@RequestBody VerificationCodeDto verificationCodeDto){
        ResponseApi transaction = transactionService.transaction(verificationCodeDto);
        if (transaction.isSuccess()) return ResponseEntity.ok(transaction);
        return ResponseEntity.status(409).body(transaction);
    }
}
