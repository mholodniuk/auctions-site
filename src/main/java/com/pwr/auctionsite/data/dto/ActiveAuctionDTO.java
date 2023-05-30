package com.pwr.auctionsite.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActiveAuctionDTO(Long auctionId,
                               Integer currentBidUserId,
                               Long sellerId,
                               BigDecimal buyNowPrice,
                               BigDecimal startingPrice,
                               BigDecimal currentBid,
                               LocalDateTime expirationDate,
                               LocalDateTime modifiedAt,
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