package com.example.p2ptransaction.repositories;
import com.example.p2ptransaction.entity.OTPTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPTransactionRepository extends JpaRepository<OTPTransaction, Long> {
    Optional<OTPTransaction> findById(Long id);
}
