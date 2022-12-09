package com.example.p2ptransaction.service;

import com.example.p2ptransaction.entity.OTPCard;
import com.example.p2ptransaction.repositories.OTPCardRepository;
import com.example.p2ptransaction.service.interfaces.OTPCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OTPCardServiceImpl implements OTPCardService {
    private final OTPCardRepository otpCardRepository;
    @Override
    public OTPCard save(OTPCard otpCard) {
        return otpCardRepository.save(otpCard);
    }
    @Override
    public Optional<OTPCard> findById(Long id) {
        return otpCardRepository.findById(id);
    }
}
