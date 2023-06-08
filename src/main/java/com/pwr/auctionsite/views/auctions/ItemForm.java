package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.ItemDTO;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemForm extends FormLayout {
    TextField itemNameField = new TextField("Item name");
    TextField itemDescriptionField = new TextField("Item description");
    TextField imageUrlField = new TextField("Image url (mock)");
    ComboBox<String> itemCategorySelect = new ComboBox<>("Category");

    public ItemForm(List<String> availableCategories) {
        addClassName("contact-form");
        getStyle().set("margin", "0 auto");
        itemCategorySelect.setItems(availableCategories);
        add(itemCategorySelect, itemNameField, itemDescriptionField, imageUrlField);
    }

    public void setItemDto(ItemDTO item) {
        if (item != null) {
            itemNameField.setValue(item.getName());
            itemDescriptionField.setValue(item.getDescription());
            imageUrlField.setValue(item.getImageUrl());
            itemCategorySelect.setValue(item.getCategory());
        } else {
            itemNameField.setValue(itemNameField.getEmptyValue());
            itemDescriptionField.setValue(itemDescriptionField.getEmptyValue());
            itemCategorySelect.setValue(itemCategorySelect.getEmptyValue());
            imageUrlField.setValue(imageUrlField.getEmptyValue());
        }
    }

}