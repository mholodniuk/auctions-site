package com.pwr.auctionsite.data.mapper;

import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class ActiveAuctionRowMapper implements RowMapper<ActiveAuctionDTO> {

    @Override
    public ActiveAuctionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        int auctionId = rs.getInt("auction_id");
        Integer currentBidUserId = rs.getInt("current_bid_user_id");
        int sellerId = rs.getInt("seller_id");
        BigDecimal buyNowPrice = rs.getBigDecimal("buy_now_price");
        BigDecimal startingPrice = rs.getBigDecimal("starting_price");
        BigDecimal currentBid = rs.getBigDecimal("current_bid");
        LocalDateTime expirationDate = rs.getTimestamp("expiration_date").toLocalDateTime();
        Integer itemQuantity = rs.getInt("item_quantity");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String imageUrl = rs.getString("image_url");
        String category = rs.getString("category");
        String seller = rs.getString("seller");
        String sellerEmail = rs.getString("seller_email");
        String buyer = rs.getString("buyer");
        String buyerEmail = rs.getString("buyer_email");

        return new ActiveAuctionDTO(auctionId, currentBidUserId, sellerId, buyNowPrice, startingPrice,
                currentBid, expirationDate, itemQuantity, name, description, imageUrl, category,
                seller, sellerEmail, buyer, buyerEmail);
    }
}

