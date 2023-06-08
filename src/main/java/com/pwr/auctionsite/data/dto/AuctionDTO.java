package com.pwr.auctionsite.data.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuctionDTO {
    private Long itemId;

    @Size(message = "item quantity should be bigger than 0")
    private Integer itemQuantity;

    @Size(message = "starting price should be bigger than 0")
    private BigDecimal startingPrice;

    @Size(message = "buy now price should be bigger than 0")
    private BigDecimal buyNowPrice;

    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expirationDate;
}
