package com.pwr.auctionsite.data.dao;

import com.pwr.auctionsite.data.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}
