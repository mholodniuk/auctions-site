package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.ActiveAuctionDAO;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuctionService {
    private final ActiveAuctionDAO activeAuctionDAO;

    public List<ActiveAuctionDTO> findAllAuctions() {
        return activeAuctionDAO.findAll();
    }
}