package com.pwr.auctionsite.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDTO {
    @NotBlank(message = "item name can not be blank")
    @Size(max = 32, message = "Item name can not exceed 32 characters")
    private String name;

    @NotBlank(message = "item category can not be blank")
    private String category;

    private String description;

    private String imageUrl;
}
