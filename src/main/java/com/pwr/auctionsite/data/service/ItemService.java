package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.ItemRepository;
import com.pwr.auctionsite.data.dto.ItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemDTO findById(Long itemId) {
        var item = itemRepository.findById(itemId).orElseThrow();

        var dto = new ItemDTO();
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setCategory(item.getCategory().getName());
        dto.setImageUrl(item.getImageUrl());

        return dto;
    }
}
