package com.pwr.auctionsite.views;

import com.pwr.auctionsite.security.SecurityService;
import com.pwr.auctionsite.views.account.AccountView;
import com.pwr.auctionsite.views.account.MyAuctionsView;
import com.pwr.auctionsite.views.auctions.ActiveAuctionsListView;
import com.pwr.auctionsite.views.auctions.ArchivedAuctionsListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();

        VaadinSession.getCurrent().setErrorHandler(new CustomErrorHandler());
    }

    private void createHeader() {
        H1 logo = new H1("Auctions site");
        logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        UserDetails userDetails = securityService.getAuthenticatedUser();

        var header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);

        if (userDetails != null) {
            Button logout = new Button("Log out", e -> securityService.logout());
            Button account = new Button(userDetails.getUsername(), new Icon(VaadinIcon.USER));
            account.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("account")));
            account.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            account.setIconAfterText(true);

            header.add(account, logout);
        } else {
            Button login = new Button("Log in", e -> UI.getCurrent().navigate("login"));
            Button signUp = new Button("Sign up", e -> UI.getCurrent().navigate("sign-up"));

            login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            signUp.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            header.add(login, signUp);
        }
        addToNavbar(header);
    }

    private void createDrawer() {
        var authenticatedUser = securityService.getAuthenticatedUser();
        var availableRoutes = new ArrayList<RouterLink>();

        availableRoutes.add(new RouterLink("Explore auctions", ActiveAuctionsListView.class));

        if (authenticatedUser != null) {
            availableRoutes.add(new RouterLink("My auctions", MyAuctionsView.class));
            availableRoutes.add(new RouterLink("Archive", ArchivedAuctionsListView.class));

            var isAdmin = authenticatedUser.getAuthorities()
                    .stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
//            if (isAdmin) {
//                availableRoutes.add(new RouterLink("Dashboard", noop.class));
//            }
            var acc = new RouterLink("My account", AccountView.class);
            acc.setVisible(false);
            availableRoutes.add(acc);
        }

        var verticalLayout = new VerticalLayout();
        availableRoutes.forEach(verticalLayout::add);
        addToDrawer(verticalLayout);
    }
}