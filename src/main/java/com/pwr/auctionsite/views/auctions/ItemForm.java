package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.ItemDTO;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemForm extends FormLayout {
    TextField itemNameField = new TextField("Item name");
    TextField itemDescriptionField = new TextField("Item description");
    TextField imageUrl = new TextField("Image url (mock)");
    ComboBox<String> itemCategory = new ComboBox<>("Category");

    public ItemForm(List<String> availableCategories) {
        addClassName("contact-form");
        getStyle().set("margin", "0 auto");
        itemCategory.setItems(availableCategories);
        add(itemCategory, itemNameField, itemDescriptionField, imageUrl);
    }

    public void setItemDto(ItemDTO item) {
        if (item != null) {
            itemNameField.setValue(item.getName());
            itemDescriptionField.setValue(item.getDescription());
            imageUrl.setValue(item.getImageUrl());
            itemCategory.setValue(item.getCategory());
        }
    }

}