package com.pwr.auctionsite.views.list;

import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.security.SecurityService;
import com.pwr.auctionsite.views.MainLayout;
import com.pwr.auctionsite.views.form.ActiveAuctionView;
import com.pwr.auctionsite.views.form.ContactForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.time.format.DateTimeFormatter;

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
    private final AuctionService auctionService;

    public ActiveAuctionsListView(AuctionService auctionService, SecurityService securityService) {
        this.auctionService = auctionService;
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
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(Alignment.CENTER);
        dialogLayout.getStyle()
                .set("width", "48rem")
                .set("max-width", "100%");

        return dialogLayout;
    }

    private void updateList() {
        grid.setItems(query -> auctionService.findAuctions(query.getOffset(), query.getLimit()).stream());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setPageSize(50);
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

    private ComponentRenderer<ActiveAuctionView, ActiveAuctionDTO> createAuctionDetailsRenderer() {
        return new ComponentRenderer<>(ActiveAuctionView::new, ActiveAuctionView::setAuction);
    }
}