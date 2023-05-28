package com.pwr.auctionsite.views.form;

import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Stream;

public class ActiveAuctionView extends FormLayout {
    private final TextField auctionId = new TextField("Auction ID");
    private final TextField category = new TextField("Category");
    private final TextField sellerName = new TextField("Seller name");
    private final TextField sellerEmail = new TextField("Seller e-mail");
    private final TextField itemQuantity = new TextField("Item quantity");
    private final TextField description = new TextField("Item description");
    private final TextField imageUrl = new TextField("Image url");
    private final TextField currentWinner = new TextField("Current winner contact");
    private final TextField startingPrice = new TextField("Starting price");
    private final NumberField newUserBid = new NumberField("Your bid");
    private final Button placeBid = new Button("Place bid");
    private final Image image = new Image("images/empty-plant.png", "Alt");

    public ActiveAuctionView() {
        configureImage();
        Stream.of(description, sellerName, sellerEmail, itemQuantity, imageUrl,
                currentWinner, startingPrice, auctionId, category).forEach(field -> {
            field.setReadOnly(true);
            add(field);
        });


        setResponsiveSteps(new ResponsiveStep("0", 4));
        setColspan(description, 4);
        setColspan(sellerName, 2);
        setColspan(sellerEmail, 2);
        setColspan(auctionId, 1);
        setColspan(category, 1);

        // bad solution but probably necessary
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            configurePlaceBidButton();
            setColspan(newUserBid, 1);
            setColspan(placeBid, 1);
        }
    }

    public void setAuction(ActiveAuctionDTO auction) {
        configurePlaceBidField(auction);
        auctionId.setValue(fillTextField(String.valueOf(auction.auctionId())));
        sellerName.setValue(fillTextField(auction.seller()));
        sellerEmail.setValue(fillTextField(auction.sellerEmail()));
        itemQuantity.setValue(fillTextField(String.valueOf(auction.itemQuantity())));
        description.setValue(fillTextField(auction.description()));
        imageUrl.setValue(fillTextField(auction.imageUrl()));
        currentWinner.setValue(fillTextField(auction.buyerEmail()));
        category.setValue(fillTextField(auction.category()));
        startingPrice.setValue(fillTextField(String.valueOf(auction.startingPrice())));
    }

    private void configurePlaceBidButton() {
        placeBid.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        placeBid.addClickListener(event -> {
            System.out.println(newUserBid.getValue());
        });
        add(newUserBid);
        add(placeBid);
    }

    private void configurePlaceBidField(ActiveAuctionDTO auction) {
        double currentBid = auction.currentBid() != null
                ? auction.currentBid().doubleValue() + 1
                : auction.startingPrice().doubleValue() + 1;

        newUserBid.setMin(currentBid);
        newUserBid.setStep(0.5);
        newUserBid.setValue(currentBid);
        newUserBid.setStepButtonsVisible(true);
        newUserBid.setErrorMessage("Your bid must be higher than %s".formatted(currentBid));
    }

    private void configureImage() {
        image.addClassName("sample-image");
        image.setMaxHeight("20rem");
        image.setMaxWidth("20rem");
        add(image);
    }

    private String fillTextField(String fieldName) {
        return fieldName != null ? fieldName : "empty";
    }
}