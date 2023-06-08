package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.AuctionDTO;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionForm extends FormLayout {
    private final NumberField itemQuantityField = new NumberField("Item quantity");
    private final BigDecimalField startingPriceField = new BigDecimalField("Starting price");
    private final BigDecimalField buyNowPriceField = new BigDecimalField("Buy now price");
    private final DateTimePicker expirationDatePicker = new DateTimePicker("Expiraction date");

    public AuctionForm() {
        addClassName("contact-form");
        getStyle().set("margin", "0 auto");
        expirationDatePicker.setMin(LocalDateTime.now());
        itemQuantityField.setMin(1);
        add(itemQuantityField, startingPriceField, buyNowPriceField, expirationDatePicker);
    }

    public void setAuctionDto(AuctionDTO auction) {
        if (auction != null) {
            itemQuantityField.setValue(auction.getItemQuantity().doubleValue());
            startingPriceField.setValue(auction.getStartingPrice());
            buyNowPriceField.setValue(auction.getBuyNowPrice());
            expirationDatePicker.setValue(auction.getExpirationDate());
        } else {
            itemQuantityField.setValue(itemQuantityField.getEmptyValue());
            startingPriceField.setValue(startingPriceField.getEmptyValue());
            buyNowPriceField.setValue(buyNowPriceField.getEmptyValue());
            expirationDatePicker.setValue(expirationDatePicker.getEmptyValue());
        }
    }
}
