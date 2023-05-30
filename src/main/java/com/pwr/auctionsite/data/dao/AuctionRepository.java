package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}