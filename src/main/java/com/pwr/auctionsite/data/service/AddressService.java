package com.pwr.auctionsite.data.service;

import com.pwr.auctionsite.data.benchmark.TrackExecutionTime;
import com.pwr.auctionsite.data.dao.ShippingAddressRepository;
import com.pwr.auctionsite.data.dto.NewAddressDTO;
import com.pwr.auctionsite.data.entity.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class AddressService {
    private final ShippingAddressRepository addressRepository;

    @Transactional
    @TrackExecutionTime
    public ShippingAddress store(NewAddressDTO addressDTO) {
        var address = new ShippingAddress();
        address.setCountry(addressDTO.getCountry());
        address.setCity(addressDTO.getCity());
        address.setStreetName(addressDTO.getStreetName());
        address.setStreetNumber(addressDTO.getStreetNumber());
        address.setZipCode(addressDTO.getPostCode());

        addressRepository.save(address);
        log.info("Saved address with id = " + address.getId());
        return address;
    }
}
