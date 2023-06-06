package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.AuctionDAO;
import com.pwr.auctionsite.data.dao.ItemCategoryRepository;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.pwr.auctionsite.data.dto.FinishedAuctionDTO;
import com.pwr.auctionsite.data.entity.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuctionService {
    private final AuctionDAO auctionDAO;
    private final ItemCategoryRepository itemCategoryRepository;

    public List<String> findAllCategories() {
        return itemCategoryRepository.findAll().stream().map(ItemCategory::getName).toList();
    }

    public List<ActiveAuctionDTO> findAuctions(String searchString, String category, int offset, int limit) {
        log.info("getting new auctions");
        return auctionDAO.findAllPaged(searchString, category, offset, limit);
    }

    public List<FinishedAuctionDTO> findArchivedAuctions(Long userId, int offset, int limit) {
        log.info("getting new auctions");
        return auctionDAO.findArchivedAuctions(userId, offset, limit);
    }

    public List<ActiveAuctionDTO> findMyAuctions(int offset, int limit, long userId, String relationType) {
        log.info("getting my auctions");
        return auctionDAO.findMyAuctions(offset, limit, userId, relationType);
    }

    public void placeBid(Long auctionId, Long userId, BigDecimal bidValue) {
        log.info("placing bid: %s to auction: %s by user %s".formatted(bidValue, auctionId, userId));
        auctionDAO.placeBid(auctionId, userId, bidValue);
    }

    public void buyNow(Long auctionId, Long userId) {
        log.info("auction: %s bought by user %s".formatted(auctionId, userId));
        auctionDAO.buyNow(auctionId, userId);
    }

    public void addAuctionToWatchlist(Long userId, Long auctionId, String relation) {
        log.info("adding: %s auction to following of %s".formatted(auctionId, userId));
        auctionDAO.addAuctionToWatchlist(userId, auctionId, relation);
    }
}