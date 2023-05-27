package com.pwr.auctionsite.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActiveAuctionDTO(int auctionId,
                               Integer currentBidUserId,
                               int sellerId,
                               BigDecimal buyNowPrice,
                               BigDecimal startingPrice,
                               BigDecimal currentBid,
                               LocalDateTime expirationDate,
                               Integer itemQuantity,
                               String name,
                               String description,
                               String imageUrl,
                               String category,
                               String seller,
                               String sellerEmail,
                               String buyer,
                               String buyerEmail) implements Serializable {
}