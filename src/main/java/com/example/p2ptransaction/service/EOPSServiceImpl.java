package com.example.p2ptransaction.service;

import com.example.p2ptransaction.entity.EOPS;
import com.example.p2ptransaction.repositories.EOPSRepository;
import com.example.p2ptransaction.service.interfaces.EOPSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class EOPSServiceImpl implements EOPSService {

    private final EOPSRepository eopsRepository;

    @Override
    public Optional<EOPS> findById(Long id) {
        return eopsRepository.findById(id);
    }
}
