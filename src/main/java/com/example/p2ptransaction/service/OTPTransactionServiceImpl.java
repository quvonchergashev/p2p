package com.example.p2ptransaction.service;

import com.example.p2ptransaction.entity.OTPCard;
import com.example.p2ptransaction.entity.OTPTransaction;
import com.example.p2ptransaction.repositories.OTPCardRepository;
import com.example.p2ptransaction.repositories.OTPTransactionRepository;
import com.example.p2ptransaction.service.interfaces.OTPCardService;
import com.example.p2ptransaction.service.interfaces.OTPTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OTPTransactionServiceImpl implements OTPTransactionService {

    private final OTPTransactionRepository otpTransactionRepository;

    @Override
    public OTPTransaction save(OTPTransaction otpTransaction) {
        return otpTransactionRepository.save(otpTransaction);
    }
    @Override
    public Optional<OTPTransaction> findById(Long id) {
        return otpTransactionRepository.findById(id);
    }
}
