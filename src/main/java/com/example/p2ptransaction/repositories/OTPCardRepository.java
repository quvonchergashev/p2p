package com.example.p2ptransaction.repositories;

import com.example.p2ptransaction.entity.OTPCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPCardRepository extends JpaRepository<OTPCard, Long> {

    Optional<OTPCard> findById(Long id);
}
