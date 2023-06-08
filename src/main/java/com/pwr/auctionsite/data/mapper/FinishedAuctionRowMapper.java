package com.pwr.auctionsite.data.mapper;

import com.pwr.auctionsite.data.dto.views.FinishedAuctionDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class FinishedAuctionRowMapper implements RowMapper<FinishedAuctionDTO> {
    @Override
    public FinishedAuctionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long auctionId = rs.getLong("auction_id");
        Integer winnerId = rs.getInt("winner_id");
        Long sellerId = rs.getLong("seller_id");
        BigDecimal finalPrice = rs.getBigDecimal("final_price");
        LocalDateTime finishedAt = rs.getTimestamp("finished_at").toLocalDateTime();
        Integer itemQuantity = rs.getInt("item_quantity");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String imageUrl = rs.getString("image_url");
        String category = rs.getString("category");
        String seller = rs.getString("seller");
        String sellerEmail = rs.getString("seller_email");
        String buyer = rs.getString("buyer");
        String buyerEmail = rs.getString("buyer_email");

        return new FinishedAuctionDTO(auctionId, winnerId, sellerId, finalPrice,
                finishedAt, itemQuantity, name, description, imageUrl, category,
                seller, sellerEmail, buyer, buyerEmail);
    }
}
