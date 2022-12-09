package com.example.p2ptransaction.service.interfaces;

import com.example.p2ptransaction.entity.OTPCard;
import com.example.p2ptransaction.entity.OTPTransaction;

import java.util.Optional;

public interface OTPTransactionService {
    OTPTransaction save(OTPTransaction otpTransaction);
    Optional<OTPTransaction> findById(Long id);






}
