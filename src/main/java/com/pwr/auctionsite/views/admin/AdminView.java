package com.pwr.auctionsite.views.admin;


import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin panel")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {
    private final Checkbox test = new Checkbox("TEST");

    public AdminView() {
        add(test);
    }
}
