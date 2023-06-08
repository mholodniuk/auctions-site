package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.views.ActiveAuctionDTO;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextField;

import java.util.stream.Stream;

public class ActiveAuctionDetails extends FormLayout {
    private final TextField auctionId = new TextField("Auction ID");
    private final TextField category = new TextField("Category");
    private final TextField sellerName = new TextField("Seller name");
    private final TextField sellerEmail = new TextField("Seller e-mail");
    private final TextField itemQuantity = new TextField("Item quantity");
    private final TextField description = new TextField("Item description");
    private final TextField imageUrl = new TextField("Image url");
    private final TextField currentWinner = new TextField("Current winner contact");
    private final TextField startingPrice = new TextField("Starting price");
    private final Image image = new Image("images/empty-plant.png", "Alt");

    public ActiveAuctionDetails(ActiveAuctionDTO activeAuction) {
        setAuction(activeAuction);
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
    }

    private void setAuction(ActiveAuctionDTO auction) {
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