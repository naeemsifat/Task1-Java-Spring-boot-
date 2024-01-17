package com.example.task1.repository;

import com.example.task1.entity.CardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardDetailsRepository extends JpaRepository<CardDetails, Long> {
    Optional<CardDetails> findByKnitCardNumber(Integer knitCardNumber);
}
