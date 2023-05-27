package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
