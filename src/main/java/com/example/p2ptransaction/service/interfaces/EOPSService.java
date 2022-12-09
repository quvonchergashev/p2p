package com.example.p2ptransaction.service.interfaces;

import com.example.p2ptransaction.entity.EOPS;

import java.util.Optional;

public interface EOPSService {

    Optional<EOPS> findById(Long id);
}
