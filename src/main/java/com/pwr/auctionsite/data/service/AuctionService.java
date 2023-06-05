package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.ActiveAuctionDAO;
import com.pwr.auctionsite.data.dao.ItemCategoryRepository;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
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
    private final ActiveAuctionDAO activeAuctionDAO;
    private final ItemCategoryRepository itemCategoryRepository;

    public List<String> findAllCategories() {
        return itemCategoryRepository.findAll().stream().map(ItemCategory::getName).toList();
    }

    public List<ActiveAuctionDTO> findAuctions(String searchString, String category, int offset, int limit) {
        log.info("getting new auctions");
        return activeAuctionDAO.findAllPaged(searchString, category, offset, limit);
    }

    public List<ActiveAuctionDTO> findMyAuctions(int offset, int limit, long userId, String relationType) {
        log.info("getting my auctions");
        return activeAuctionDAO.findMyAuctions(offset, limit, userId, relationType);
    }

    public void placeBid(Long auctionId, Long userId, BigDecimal bidValue) {
        log.info("placing bid: %s to auction: %s by user %s".formatted(bidValue, auctionId, userId));
        activeAuctionDAO.placeBid(auctionId, userId, bidValue);
    }

    public void buyNow(Long auctionId, Long userId) {
        log.info("auction: %s bought by user %s".formatted(auctionId, userId));
        activeAuctionDAO.buyNow(auctionId, userId);
    }

    public void addAuctionToWatchlist(Long userId, Long auctionId, String relation) {
        log.info("adding: %s auction to following of %s".formatted(auctionId, userId));
        activeAuctionDAO.addAuctionToWatchlist(userId, auctionId, relation);
    }
}