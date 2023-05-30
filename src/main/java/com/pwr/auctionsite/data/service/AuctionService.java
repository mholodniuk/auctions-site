package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.ActiveAuctionDAO;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuctionService {
    private final ActiveAuctionDAO activeAuctionDAO;

    public List<ActiveAuctionDTO> findAuctions(int offset, int limit) {
        log.info("getting new auctions");
        return activeAuctionDAO.findAllPaged(offset, limit);
    }

    public void placeBid(Long auctionId, Long userId, BigDecimal bidValue) {
        log.info("placing bid: %s to auction: %s by user %s".formatted(bidValue, auctionId, userId));
        activeAuctionDAO.placeBid(auctionId, userId, bidValue);
    }

    public void buyNow(Long auctionId, Long userId) {
        log.info("auction: %s bought by user %s".formatted(auctionId, userId));
        activeAuctionDAO.buyNow(auctionId, userId);
    }
}