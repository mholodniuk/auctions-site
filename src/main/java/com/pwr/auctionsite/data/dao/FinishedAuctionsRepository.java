package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.FinishedAuction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinishedAuctionsRepository extends JpaRepository<FinishedAuction, Long> {
}
