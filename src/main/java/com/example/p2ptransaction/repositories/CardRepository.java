package com.example.p2ptransaction.repositories;

import com.example.p2ptransaction.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByToken(String token);

    List<Card> findAllByUserId(Long userId);
}
