package com.pwr.auctionsite.views.signup;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

@Getter
public class AccountCreationForm extends HorizontalLayout {
    private final TextField usernameField = new TextField("Username");
    private final TextField firstNameField = new TextField("First name");
    private final TextField lastNameField = new TextField("Last name");
    private final EmailField emailField = new EmailField("E-mail");
    private final PasswordField passwordField = new PasswordField("Password");

    public AccountCreationForm() {
        FormLayout formLayout = new FormLayout(usernameField, firstNameField, lastNameField, emailField, passwordField);
        formLayout.setMaxWidth("600px");
        formLayout.getStyle().set("margin", "0 auto");
        add(formLayout);
    }
}
