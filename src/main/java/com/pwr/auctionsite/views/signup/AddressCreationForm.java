package com.pwr.auctionsite.views.signup;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

@Getter
public class AddressCreationForm extends HorizontalLayout {
    private final TextField countryField = new TextField("Country");
    private final TextField cityField = new TextField("City");
    private final TextField streetNameField = new TextField("Street name");
    private final TextField streetNumberField = new TextField("Street number");
    private final TextField postCodeField = new TextField("Postal code");

    public AddressCreationForm() {
        FormLayout formLayout = new FormLayout(countryField, cityField, streetNameField, streetNumberField, postCodeField);
        formLayout.setMaxWidth("600px");
        formLayout.getStyle().set("margin", "0 auto");
        add(formLayout);
    }
}
