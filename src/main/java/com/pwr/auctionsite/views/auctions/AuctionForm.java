package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.entity.Auction;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.data.service.ItemService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

public class AuctionForm extends FormLayout {
    TextField itemQuantity = new TextField("Item quantity");
    ComboBox<String> itemCategory = new ComboBox<>("Status");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    private String currentUrl = "";

    Binder<Auction> binder = new BeanValidationBinder<>(Auction.class);

    public AuctionForm(@Autowired AuctionService auctionService,
                       @Autowired ItemService itemService) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        add(itemQuantity, itemCategory, createButtonsLayout());
    }

    public void setContact(Auction auction) {
        binder.setBean(auction);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class AuctionFormEvent extends ComponentEvent<AuctionForm> {
        private final Auction auction;

        protected AuctionFormEvent(AuctionForm source, Auction auction) {
            super(source, false);
            this.auction = auction;
        }

        public Auction getAuction() {
            return auction;
        }
    }

    public static class SaveEvent extends AuctionFormEvent {
        SaveEvent(AuctionForm source, Auction auction) {
            super(source, auction);
        }
    }

    public static class DeleteEvent extends AuctionFormEvent {
        DeleteEvent(AuctionForm source, Auction auction) {
            super(source, auction);
        }
    }

    public static class CloseEvent extends AuctionFormEvent {
        CloseEvent(AuctionForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}