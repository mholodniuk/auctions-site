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
public class NewAddressDTO {
    @NotBlank(message = "country name can not be blank")
    @Size(max = 20, message = "country name can not exceed 20 characters")
    private String country;

    @NotBlank(message = "city name can not be blank")
    @Size(max = 32, message = "city name can not exceed 32 characters")
    private String city;

    @NotBlank(message = "street number can not be blank")
    @Size(max = 20, message = "street number can not exceed 20 characters")
    private String streetNumber;

    @NotBlank(message = "street name  can not be blank")
    @Size(max = 20, message = "street name can not exceed 20 characters")
    private String streetName;

    @NotBlank(message = "post code can not be blank")
    @Size(max = 20, message = "pot code can not exceed 20 characters")
    private String postCode;
}
