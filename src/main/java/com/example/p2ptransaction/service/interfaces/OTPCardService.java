package com.example.p2ptransaction.service.interfaces;

import com.example.p2ptransaction.entity.OTPCard;
import com.example.p2ptransaction.entity.OTPTransaction;

import java.util.Optional;

public interface OTPCardService {
    OTPCard save(OTPCard otpCard);
    Optional<OTPCard> findById(Long id);






}
