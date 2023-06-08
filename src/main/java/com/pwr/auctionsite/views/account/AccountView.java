package com.pwr.auctionsite.views.account;

import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.security.SecurityService;
import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;


// todo: add new form that represents all account data
// here user can change data (re-use sign-up components)

@Route(value = "account", layout = MainLayout.class)
@PageTitle("Account")
@PermitAll
public class AccountView extends VerticalLayout {

    public AccountView(@Autowired AuctionService auctionService,
                       @Autowired SecurityService securityService) {
        Button button = new Button();
        button.setText("Go to my auctions");
        button.addClickListener(e -> UI.getCurrent().navigate("account/auctions"));
        add(button);
    }
}
