package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.ActiveAuctionDTO;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.security.SecurityService;
import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.time.format.DateTimeFormatter;

// todo: nie działa do końca dodawania aukcji do obserwowanych

@AnonymousAllowed
@Route(value = "", layout = MainLayout.class)
@PageTitle("Auctions")
public class ActiveAuctionsListView extends VerticalLayout {
    Grid<ActiveAuctionDTO> grid = new Grid<>(ActiveAuctionDTO.class, false);
    TextField filterText = new TextField();
    ActiveAuctionActionsForm activeAuctionActionsForm;
    Dialog dialog = new Dialog();
    private final AuctionService auctionService;
    private final SecurityService securityService;

    public ActiveAuctionsListView(AuctionService auctionService,
                                  SecurityService securityService) {
        this.auctionService = auctionService;
        this.securityService = securityService;
        setSizeFull();
        addClassName("list-view");
        configureForm();
        configureDialog();
        configureGrid();
        add(getToolbar(), getContent());
        updateList();
    }

    private void configureDialog() {
        dialog.setHeaderTitle("Actions");
        HorizontalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);
        dialog.addDialogCloseActionListener(e -> closeEditor());
        add(dialog);
    }

    private HorizontalLayout createDialogLayout() {
        HorizontalLayout dialogLayout = new HorizontalLayout(activeAuctionActionsForm);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(Alignment.CENTER);

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
        grid.addColumn(ActiveAuctionDTO::currentBid).setHeader("Current Bid").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(ActiveAuctionDTO::buyNowPrice).setHeader("Buy Now Price").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(auction -> auction.expirationDate().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")))
                .setHeader("Expiration date");
        grid.addColumn(auction -> auction.modifiedAt().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")))
                .setHeader("Last modification");
        grid.addComponentColumn(this::createActionButton).setTextAlign(ColumnTextAlign.CENTER);
        grid.setItemDetailsRenderer(createAuctionDetailsRenderer());
    }

    private void configureForm() {
        activeAuctionActionsForm = new ActiveAuctionActionsForm(auctionService, securityService);
        activeAuctionActionsForm.setSizeFull();
    }

    private Button createActionButton(ActiveAuctionDTO auction) {
        var actionButton = new Button();
        actionButton.setIcon(new Icon("vaadin", "ellipsis-dots-v"));
        actionButton.setEnabled(securityService.getAuthenticatedUser() != null);
        actionButton.setMaxHeight("2rem");
        actionButton.addClickListener(event -> {
            activeAuctionActionsForm.setAuction(auction);
            dialog.open();
        });
        return actionButton;
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button queryButton = new Button("Search");

        var toolbar = new HorizontalLayout(filterText, queryButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void closeEditor() {
        removeClassName("editing");
        dialog.close();
        updateList();
    }

    private ComponentRenderer<ActiveAuctionDetails, ActiveAuctionDTO> createAuctionDetailsRenderer() {
        return new ComponentRenderer<>(ActiveAuctionDetails::new);
    }
}