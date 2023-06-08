package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.ItemCategoryRepository;
import com.pwr.auctionsite.data.entity.ItemCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemCategoryService {
    private final ItemCategoryRepository itemCategoryRepository;

    public List<String> findAllCategories() {
        return itemCategoryRepository.findAll().stream().map(ItemCategory::getName).toList();
    }

    public void addCategory(String name) {
        var category = new ItemCategory();
        category.setName(name);
        itemCategoryRepository.save(category);
    }
}
