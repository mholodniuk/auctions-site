package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.*;
import com.pwr.auctionsite.data.dto.AuctionDataDTO;
import com.pwr.auctionsite.data.dto.views.ActiveAuctionDTO;
import com.pwr.auctionsite.data.dto.views.FinishedAuctionDTO;
import com.pwr.auctionsite.data.entity.Auction;
import com.pwr.auctionsite.data.entity.Item;
import com.pwr.auctionsite.data.entity.ItemCategory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuctionService {
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final ItemRepository itemRepository;
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

    @Transactional
    public void saveAuction(AuctionDataDTO auctionData, Long userId) {
        var itemCategory = itemCategoryRepository.findByName(auctionData.getCategory()).orElseThrow();
        var seller = userRepository.findById(userId).orElseThrow();

        var item = Item.builder()
                .name(auctionData.getName())
                .description(auctionData.getDescription())
                .imageUrl(auctionData.getImageUrl())
                .modifiedAt(LocalDateTime.now())
                .category(itemCategory)
                .build();

        itemRepository.save(item);

        var auction = Auction.builder()
                .itemQuantity(auctionData.getItemQuantity())
                .startingPrice(auctionData.getStartingPrice())
                .buyNowPrice(auctionData.getBuyNowPrice())
                .expirationDate(auctionData.getExpirationDate())
                .seller(seller)
                .item(item)
                .build();

        auctionRepository.save(auction);
    }

    @Transactional
    public void editAuction(AuctionDataDTO auctionData, Long auctionId) {
        var itemCategory = itemCategoryRepository.findByName(auctionData.getCategory()).orElseThrow();
        var auction = auctionRepository.findById(auctionId).orElseThrow();
        var item = itemRepository.findById(auction.getItem().getId()).orElseThrow();

        item.setName(auctionData.getName());
        item.setDescription(auctionData.getDescription());
        item.setImageUrl(auctionData.getImageUrl());
        item.setModifiedAt(LocalDateTime.now());
        item.setCategory(itemCategory);

        auction.setItemQuantity(auction.getItemQuantity());
        auction.setBuyNowPrice(auctionData.getBuyNowPrice());
        auction.setExpirationDate(auctionData.getExpirationDate());
    }

    public void placeBidProcedure(Long auctionId, Long userId, BigDecimal bidValue) {
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

    @Scheduled(cron = "0 */5 * ? * *")
    public void clearExpiredAuctions() {
        var expiredAuctions = auctionDAO.findExpiredAuctions();
        log.info("executing background job at {}. Affected auctions: {}", LocalDateTime.now(), expiredAuctions.size());
        var procedure = " CALL move_auction_to_finished(?, ?)";
        for (var auction : expiredAuctions) {
            try {
                template.update(procedure, auction.auctionId(), auction.bid() != null ? auction.bid() : BigDecimal.ZERO);
            } catch (Exception e) {
                log.error("failed to move auction with id {} to finished", auction.auctionId());
            }
            log.info("auction with id {} was moved to finished ", auction.auctionId());
        }
    }

    public void deleteById(Long auctionId) {
        auctionRepository.deleteById(auctionId);
    }
}