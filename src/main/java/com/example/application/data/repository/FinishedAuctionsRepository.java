package com.example.application.data.repository;

import com.example.application.data.entity.FinishedAuction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinishedAuctionsRepository extends JpaRepository<FinishedAuction, Long> {
}
