package com.pwr.auctionsite.data.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
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
public class AuctionDataDTO {
    private Long itemId;

    @Size(min = 1, message = "item quantity should be bigger than 0")
    private Integer itemQuantity;

    @Size(min = 1, message = "starting price should be bigger than 0")
    private BigDecimal startingPrice;

    @Size(min = 1, message = "buy now price should be bigger than 0")
    private BigDecimal buyNowPrice;

    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expirationDate;

    @NotBlank(message = "item name can not be blank")
    @Size(max = 32, message = "Item name can not exceed 32 characters")
    private String name;

    @NotBlank(message = "item category can not be blank")
    private String category;

    @NotBlank(message = "description should not be empty")
    private String description;

    @NotBlank(message = "image url should not be empty")
    private String imageUrl;
}
