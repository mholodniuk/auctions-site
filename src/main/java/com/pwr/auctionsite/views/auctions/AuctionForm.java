package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.AuctionDTO;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AuctionForm extends FormLayout {
    private final NumberField itemQuantityField = new NumberField("Item quantity");
    private final BigDecimalField startingPriceField = new BigDecimalField("Starting price");
    private final BigDecimalField buyNowPriceField = new BigDecimalField("Buy now price");
    private final DateTimePicker expirationDatePicker = new DateTimePicker("Expiration date");
    TextField itemNameField = new TextField("Item name");
    TextField itemDescriptionField = new TextField("Item description");
    TextField imageUrlField = new TextField("Image url (mock)");
    ComboBox<String> itemCategorySelect = new ComboBox<>("Category");

    public AuctionForm(List<String> availableCategories) {
        addClassName("contact-form");
        getStyle().set("margin", "0 auto");
        expirationDatePicker.setMin(LocalDateTime.now());
        itemQuantityField.setMin(1);
        itemCategorySelect.setItems(availableCategories);
        add(itemCategorySelect, itemNameField, itemDescriptionField, imageUrlField);
        add(itemQuantityField, startingPriceField, buyNowPriceField, expirationDatePicker);
    }

    public void setAuctionDto(AuctionDTO auction) {
        if (auction != null) {
            startingPriceField.setEnabled(false);
            itemQuantityField.setEnabled(false);
            itemQuantityField.setValue(auction.getItemQuantity().doubleValue());
            startingPriceField.setValue(auction.getStartingPrice());
            buyNowPriceField.setValue(auction.getBuyNowPrice());
            expirationDatePicker.setValue(auction.getExpirationDate());
            itemNameField.setValue(auction.getName());
            itemDescriptionField.setValue(auction.getDescription());
            imageUrlField.setValue(auction.getImageUrl());
            itemCategorySelect.setValue(auction.getCategory());
        } else {
            startingPriceField.setEnabled(true);
            itemQuantityField.setEnabled(true);
            itemQuantityField.setValue(itemQuantityField.getEmptyValue());
            startingPriceField.setValue(startingPriceField.getEmptyValue());
            buyNowPriceField.setValue(buyNowPriceField.getEmptyValue());
            expirationDatePicker.setValue(expirationDatePicker.getEmptyValue());
            itemNameField.setValue(itemNameField.getEmptyValue());
            itemDescriptionField.setValue(itemDescriptionField.getEmptyValue());
            itemCategorySelect.setValue(itemCategorySelect.getEmptyValue());
            imageUrlField.setValue(imageUrlField.getEmptyValue());
        }
    }
}
