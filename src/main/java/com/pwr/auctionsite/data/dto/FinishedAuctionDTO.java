package com.pwr.auctionsite.data.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinishedAuctionDTO(Long auctionId,
                               Integer winnerId,
                               Long sellerId,
                               BigDecimal finalPrice,
                               LocalDateTime finishedAt,
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