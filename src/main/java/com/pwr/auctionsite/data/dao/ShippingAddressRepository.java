package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
}
