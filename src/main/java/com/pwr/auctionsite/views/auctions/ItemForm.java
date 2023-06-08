package com.pwr.auctionsite.views.auctions;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ItemForm extends FormLayout {
    private final TextField text = new TextField("dupa");

    public ItemForm() {
        text.setValue("HELLO");
        add(text);
    }
}
