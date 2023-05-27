package com.example.application.data.service;

import com.example.application.data.entity.Auction;
import com.example.application.data.repository.AuctionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public List<Auction> findAllAuctions() {
        return auctionRepository.findAll();
    }
}