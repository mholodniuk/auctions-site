package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.views.ActiveAuctionDTO;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.security.SecurityService;
import com.pwr.auctionsite.security.model.CustomUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;

import java.math.BigDecimal;

public class ActiveAuctionActionsForm extends FormLayout {
    private final NumberField bidField = new NumberField("Your bid");
    private final Button placeBidButton = new Button("Place bid");
    private final Button addToWatchlistButton = new Button("Add to watchlist");
    private final Button deleteAuctionButton = new Button("Move to archive");
    private final Button buyNowButton = new Button();
    private ActiveAuctionDTO auction;
    private final AuctionService auctionService;
    private final SecurityService securityService;

    public ActiveAuctionActionsForm(AuctionService auctionService, SecurityService securityService) {
        this.auctionService = auctionService;
        this.securityService = securityService;
        configurePlaceBidButton();
        configureBuyNowButton();
        configureAddToWatchlistButton();
    }

    public void setAuction(ActiveAuctionDTO auction) {
        this.auction = auction;
        configurePlaceBidField();
        if (securityService.getAuthenticatedUserRole().contains("ADMIN")) {
            configureDeleteAuctionButton();
        }
        setColspan(deleteAuctionButton, 2);
        setActive(true);
    }

    private void configureBuyNowButton() {
        buyNowButton.setText("Buy now");
        buyNowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buyNowButton.addClickListener(event -> {
            var user = (CustomUser) securityService.getAuthenticatedUser();
            auctionService.buyNow(auction.auctionId(), user.getId());
            setActive(false);
        });
        add(buyNowButton);
    }

    private void configurePlaceBidField() {
        var minBid = auction.currentBid() == null ? auction.startingPrice() :
                auction.currentBid().add(BigDecimal.valueOf(0.5));
        bidField.setMin(minBid.doubleValue());
        bidField.setValue(minBid.add(BigDecimal.ONE).doubleValue());
        bidField.setStep(0.5);
        bidField.setStepButtonsVisible(true);
        bidField.setErrorMessage("Your bid must be higher than %s".formatted(minBid.doubleValue()));
        add(bidField);
    }

    private void configurePlaceBidButton() {
        placeBidButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        placeBidButton.addClickListener(event -> {
            var user = (CustomUser) securityService.getAuthenticatedUser();
            auctionService.placeBidProcedure(auction.auctionId(), user.getId(), BigDecimal.valueOf(bidField.getValue()));
            setActive(false);
        });
        add(placeBidButton);
    }

    private void configureAddToWatchlistButton() {
        addToWatchlistButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addToWatchlistButton.addClickListener(event -> {
            var user = (CustomUser) securityService.getAuthenticatedUser();
            auctionService.addAuctionToWatchlist(user.getId(), auction.auctionId(), "FOLLOWING");
            setActive(false);
        });
        add(addToWatchlistButton);
    }

    private void configureDeleteAuctionButton() {
        deleteAuctionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteAuctionButton.addClickListener(event -> {
            auctionService.moveAuctionToFinished(auction.auctionId(), auction.currentBid());
            setActive(false);
        });
        add(deleteAuctionButton);
    }


    private void setActive(boolean enabled) {
        placeBidButton.setEnabled(enabled);
        buyNowButton.setEnabled(enabled);
        bidField.setEnabled(enabled);
        addToWatchlistButton.setEnabled(enabled);
        deleteAuctionButton.setEnabled(enabled);
    }
}
