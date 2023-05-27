package com.pwr.auctionsite.views.list;

import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

// Fajne komponenty:
// - Grid -> do wyświetlenia listy rzeczy
// - Grid/Item-details -> do wyświetlenia więcej informacji o rzeczy
// - Grid/Lazy-loading -> do paginacji między stronami w liście
// - Grid/Filters-Sorting
// - Grid/Buffered -> do edytowania wierszy inline
// - Icons


@AnonymousAllowed
@Route(value = "", layout = MainLayout.class)
@PageTitle("Auctions")
public class ActiveAuctionsListView extends VerticalLayout {
    Grid<ActiveAuctionDTO> grid = new Grid<>(ActiveAuctionDTO.class, false);
    TextField filterText = new TextField();
    ContactForm form;
    Dialog dialog = new Dialog();
    AuctionService service;

    public ActiveAuctionsListView(AuctionService service) {
        this.service = service;
        addClassName("list-view");
//        configureForm();
        configureDialog();
        setSizeFull();
        configureGrid();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureDialog() {
        dialog.setHeaderTitle("New auction");
        HorizontalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);
        add(dialog);
    }

    private HorizontalLayout createDialogLayout() {
        Image image = new Image("images/linux.png", "Alt");
        image.addClassName("sample-image");
        image.setMaxHeight("20rem");
        image.setMaxWidth("20rem");
        HorizontalLayout dialogLayout = new HorizontalLayout(image, new FormLayout());
//        dialogLayout.setPadding(false);
//        dialogLayout.setSpacing(false);
//        dialogLayout.setAlignItems(Alignment.CENTER);
//        dialogLayout.getStyle()
//                .set("width", "48rem")
//                .set("max-width", "100%");

        return dialogLayout;
    }

    private void updateList() {
        grid.setItems(service.findAllAuctions());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(ActiveAuctionDTO::name).setHeader("Item");
        grid.addColumn(ActiveAuctionDTO::category).setHeader("Category");
        grid.addColumn(ActiveAuctionDTO::buyer).setHeader("Current Winner");
        grid.addColumn(ActiveAuctionDTO::currentBid).setHeader("Current Bid");
        grid.addColumn(ActiveAuctionDTO::buyNowPrice).setHeader("Buy Now Price");
        grid.addColumn(auction -> auction.expirationDate().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")))
                .setHeader("Expiration date");
        grid.setItemDetailsRenderer(createAuctionDetailsRenderer());
//        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

//    private void configureForm() {
//        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
//        form.setWidth("32em");
//        form.setVisible(true);
//        form.addSaveListener(this::saveContact);
//        form.addDeleteListener(this::deleteContact);
//        form.addCloseListener(e -> closeEditor());
//    }

//    private void saveContact(ContactForm.SaveEvent event) {
//        service.saveContact(event.getContact());
//        updateList();
//        closeEditor();
//    }
//
//    private void deleteContact(ContactForm.DeleteEvent event) {
//        service.deleteContact(event.getContact());
//        updateList();
//        closeEditor();
//    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
//        addContactButton.addClickListener(click -> addContact());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

//    private void addContact() {
//        grid.asSingleSelect().clear();
//        editContact(new Contact());
//    }
//
//    private void editContact(Contact contact) {
//        if (contact == null) {
//            closeEditor();
//        } else {
//            form.setContact(contact);
//            addClassName("editing");
//            dialog.open();
//        }
//    }

    private void closeEditor() {
        dialog.close();
//        form.setContact(null);
        removeClassName("editing");
    }


    private static ComponentRenderer<ActiveAuctionFormLayout, ActiveAuctionDTO> createAuctionDetailsRenderer() {
        return new ComponentRenderer<>(ActiveAuctionFormLayout::new, ActiveAuctionFormLayout::setAuction);
    }

    private static class ActiveAuctionFormLayout extends FormLayout {
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

        public ActiveAuctionFormLayout() {
            configureImage();
            Stream.of(description, sellerName, sellerEmail, itemQuantity, imageUrl,
                    currentWinner, startingPrice, auctionId, category).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });
            configurePlaceBid();

            setResponsiveSteps(new ResponsiveStep("0", 4));
            setColspan(description, 4);
            setColspan(sellerName, 2);
            setColspan(sellerEmail, 2);
            setColspan(auctionId, 1);
            setColspan(category, 1);
            setColspan(newUserBid, 1);
            setColspan(placeBid, 1);
        }

        private void configureImage() {
            image.addClassName("sample-image");
            image.setMaxHeight("20rem");
            image.setMaxWidth("20rem");
            add(image);
        }

        private void configurePlaceBid() {
            // todo: handle min bid
            placeBid.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            placeBid.addClickListener(event -> {
                System.out.println(event.getButton());
                System.out.println(newUserBid.getValue());
            });
            add(newUserBid);
            add(placeBid);
        }

        public void setAuction(ActiveAuctionDTO auction) {
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

        private String fillTextField(String fieldName) {
            return fieldName != null ? fieldName : "empty";
        }
    }
}