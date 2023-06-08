package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.dao.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
}
