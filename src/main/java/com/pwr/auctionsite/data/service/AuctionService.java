package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.ActiveAuctionDAO;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AuctionService {
    private final ActiveAuctionDAO activeAuctionDAO;

    public List<ActiveAuctionDTO> findAuctions(int offset, int limit) {
        return activeAuctionDAO.findAllPaged(offset, limit);
    }

    public void buyNow(int userId, int auctionId) {
        activeAuctionDAO.buyNow(auctionId, userId);
    }
}