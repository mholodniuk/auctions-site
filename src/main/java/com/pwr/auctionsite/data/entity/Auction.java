package com.pwr.auctionsite.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auctions")
public class Auction {
    @Id
    @NotNull
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "item_quantity")
    private Integer itemQuantity;

    @NotNull
    @Column(name = "starting_price", nullable = false, precision = 7, scale = 2)
    private BigDecimal startingPrice;

    @NotNull
    @Column(name = "buy_now_price", nullable = false, precision = 7, scale = 2)
    private BigDecimal buyNowPrice;

    @Column(name = "current_bid", precision = 7, scale = 2)
    private BigDecimal currentBid;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_bid_user_id")
    private User currentBidUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
}
