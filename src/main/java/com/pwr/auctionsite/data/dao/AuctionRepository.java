package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    @Modifying
    @Query("UPDATE Auction SET currentBidUser.id = :user_id, currentBid = :bid_value WHERE id = :auction_id")
    void placeBid(@Param("auction_id") Long auctionId, @Param("user_id") Long userId, @Param("bid_value") BigDecimal bidValue);
}