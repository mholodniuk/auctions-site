package com.pwr.auctionsite.views.signup;

import com.pwr.auctionsite.data.dto.NewAddressDTO;
import com.pwr.auctionsite.data.dto.NewUserDTO;
import com.pwr.auctionsite.data.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

enum Step {
    ADDRESS, ACCOUNT
}

@Route("sign-up")
@PageTitle("Create account")
@AnonymousAllowed
public class SignUpView extends VerticalLayout {
    private Step state;
    private final UserService userService;
    private Tab createAddressTab;
    private Tab createAccountTab;
    private TabSheet tabs;
    private final AddressCreationForm addressForm;
    private final AccountCreationForm accountForm;
    private final Button saveAddressButton = new Button("Save");

    private final Button saveAccountButton = new Button("Create account");
    private final BeanValidationBinder<NewAddressDTO> addressBinder;
    private final BeanValidationBinder<NewUserDTO> accountBinder;

    public SignUpView(@Autowired UserService userService) {
        this.userService = userService;
        this.addressBinder = new BeanValidationBinder<>(NewAddressDTO.class);
        this.accountBinder = new BeanValidationBinder<>(NewUserDTO.class);
        addressForm = new AddressCreationForm();
        accountForm = new AccountCreationForm();
        state = Step.ADDRESS;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        createTabs();
        configureAddressBinder();
        configureAccountBinder();
        configureAddressButton();
    }

    private void createTabs() {
        var title = new H1("Sign up");
        add(title);
        createAddressTab = new Tab(VaadinIcon.HOME.create());
        createAccountTab = new Tab(VaadinIcon.SIGN_IN.create());
        tabs = new TabSheet();

        for (var tab : new Tab[]{createAddressTab, createAccountTab}) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        tabs.add(createAddressTab, addressForm);
        tabs.add(createAccountTab, accountForm);

        add(tabs);
    }

    // todo: create two button
    private void configureAddressButton() {
        saveAddressButton.getStyle().set("margin", "0 auto");
        saveAddressButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveAddressButton.addClickListener(click -> {
            try {
                if (state == Step.ADDRESS) {
                    changeStep(Step.ACCOUNT);
                } else {
                    saveAccountWithAddress();
                }
            } catch (Exception e) {
                changeStep(Step.ADDRESS);
                System.out.println("FAILED");
                Notification.show("Failed to save data 😥");
            }
        });
        add(saveAddressButton);
    }

    private void saveAccountWithAddress() throws ValidationException {
        var address = new NewAddressDTO();
        addressBinder.writeBean(address);
        var user = new NewUserDTO();
        accountBinder.writeBean(user);

        userService.store(user, address);
        System.out.println("Stored user");
        Notification.show("Account created");
        UI.getCurrent().navigate("/");
    }

    private void configureAddressBinder() {
        addressBinder.forField(addressForm.getCountryField()).asRequired().bind("country");
        addressBinder.forField(addressForm.getCityField()).asRequired().bind("city");
        addressBinder.forField(addressForm.getStreetNameField()).asRequired().bind("streetName");
        addressBinder.forField(addressForm.getStreetNumberField()).asRequired().bind("streetNumber");
        addressBinder.forField(addressForm.getPostCodeField()).asRequired().bind("postCode");
    }

    private void configureAccountBinder() {
        accountBinder.forField(accountForm.getUsernameField()).asRequired().bind("username");
        accountBinder.forField(accountForm.getFirstNameField()).asRequired().bind("firstName");
        accountBinder.forField(accountForm.getLastNameField()).asRequired().bind("lastName");
        accountBinder.forField(accountForm.getEmailField()).asRequired().bind("email");
        accountBinder.forField(accountForm.getPasswordField()).asRequired().bind("password");
    }

    private void changeStep(Step step) {
        if (step == Step.ACCOUNT) {
            state = Step.ACCOUNT;
            tabs.setSelectedTab(createAccountTab);
        } else if (step == Step.ADDRESS) {
            state = Step.ADDRESS;
            tabs.setSelectedTab(createAddressTab);
        }
    }
}
