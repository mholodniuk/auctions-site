package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.AuctionDAO;
import com.pwr.auctionsite.data.dao.AuctionRepository;
import com.pwr.auctionsite.data.dao.ItemCategoryRepository;
import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.pwr.auctionsite.data.dto.FinishedAuctionDTO;
import com.pwr.auctionsite.data.entity.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionDAO auctionDAO;
    private final JdbcTemplate template;
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
        log.info("placing bid %s on auction %s by user %s"
                .formatted(bidValue.doubleValue(), auctionId, bidValue.doubleValue()));
        var sql = """
                CALL place_bid(?, ?, ?)
                """;
        template.update(sql, auctionId, userId, bidValue);
    }

    public void moveAuctionToFinished(Long auctionId, BigDecimal bidValue) {
        log.info("auction: %s moved to finished with price %s"
                .formatted(auctionId, bidValue.doubleValue()));
        var sql = """
                CALL move_auction_to_finished(?, ?)
                """;
        template.update(sql, auctionId, bidValue);
    }

    public void buyNow(Long auctionId, Long userId) {
        log.info("auction: %s bought by user %s".formatted(auctionId, userId));
        var sql = """
                CALL buy_now(?, ?)
                """;
        template.update(sql, auctionId, userId);
    }

    public void addAuctionToWatchlist(Long userId, Long auctionId, String relation) {
        log.info("adding: %s auction to following of %s".formatted(auctionId, userId));
        var sql = """
                CALL add_auction_to_watchlist(?, ?, ?)
                """;
        template.update(sql, userId, auctionId, relation);
    }
}