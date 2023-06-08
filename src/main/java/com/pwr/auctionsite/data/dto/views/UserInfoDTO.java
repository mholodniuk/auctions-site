package com.pwr.auctionsite.data.dto.views;

public record UserInfoDTO(
        Long id,
        String username,
        String fullName,
        String email,
        String role,
        Boolean isBlocked,
        String country,
        String city,
        String street,
        String streetNumber
) {
}