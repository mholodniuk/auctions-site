package com.pwr.auctionsite.views.account;

import com.pwr.auctionsite.data.dto.views.ActiveAuctionDTO;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.data.service.ItemCategoryService;
import com.pwr.auctionsite.data.service.ItemService;
import com.pwr.auctionsite.security.SecurityService;
import com.pwr.auctionsite.security.model.CustomUser;
import com.pwr.auctionsite.views.MainLayout;
import com.pwr.auctionsite.views.auctions.ActiveAuctionDetails;
import com.pwr.auctionsite.views.auctions.ItemForm;
import com.pwr.auctionsite.views.auctions.AuctionForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("My auctions")
@Route(value = "account/auctions", layout = MainLayout.class)
@PermitAll
public class MyAuctionsView extends VerticalLayout {
    Grid<ActiveAuctionDTO> grid = new Grid<>(ActiveAuctionDTO.class, false);
    private final ComboBox<String> relationTypeSelector = new ComboBox<>("Relation");
    private final Button newAuctionButton = new Button("Create new auction");
    private final Button saveAuctionButton = new Button("Save auction");
    private final Button closeDialogButton = new Button("Close");

    private final Dialog dialog = new Dialog();
    private ItemForm itemForm;
    private AuctionForm auctionForm;
    private final CustomUser currentUser;
    private final AuctionService auctionService;
    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;

    public MyAuctionsView(@Autowired AuctionService auctionService,
                          @Autowired SecurityService securityService,
                          @Autowired ItemService itemService,
                          @Autowired ItemCategoryService itemCategoryService) {
        this.itemService = itemService;
        this.auctionService = auctionService;
        this.itemCategoryService = itemCategoryService;
        currentUser = (CustomUser) securityService.getAuthenticatedUser();
        setSizeFull();
        addClassName("list-view");
        configureGrid();
        configureAuctionForm();
        configureItemForm();
        configureDialog();
        configureActionButtons();
        add(getToolbar(), getContent());
    }

    private void updateList() {
        grid.setItems(query ->
                auctionService.findMyAuctions(query.getOffset(), query.getLimit(), currentUser.getId(), relationTypeSelector.getValue())
                        .stream());
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
        grid.setItemDetailsRenderer(createAuctionDetailsRenderer());
        grid.addComponentColumn(this::createEditAuctionButton).setTextAlign(ColumnTextAlign.END);
    }


    private HorizontalLayout getContent() {
        var content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        relationTypeSelector.setItems(List.of("SELLING", "FOLLOWING", "BIDING"));
        relationTypeSelector.addValueChangeListener(e -> updateList());
        newAuctionButton.addClickListener(event -> addAuction());
        var toolbar = new HorizontalLayout(relationTypeSelector, newAuctionButton);
        toolbar.setWidthFull();
        toolbar.setAlignSelf(Alignment.END, relationTypeSelector, newAuctionButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private Button createEditAuctionButton(ActiveAuctionDTO auction) {
        var actionButton = new Button("Edit");
        actionButton.setVisible(relationTypeSelector.getValue().equals("SELLING"));
        actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        actionButton.addClickListener(event -> {
            editAuction(auction);
        });
        return actionButton;
    }

    private void addAuction() {
        grid.asSingleSelect().clear();
        editAuction(null);
    }

    private void editAuction(ActiveAuctionDTO auction) {
        if (auction == null) {
            itemForm.setItemDto(null);
            auctionForm.setAuctionDto(null);
            dialog.open();
        } else {
            var auctionDto = auctionService.findById(auction.auctionId());
            var itemDto = itemService.findById(auctionDto.getItemId());
            itemForm.setItemDto(itemDto);
            auctionForm.setAuctionDto(auctionDto);
            addClassName("editing");
            dialog.open();
        }
    }

    private void closeEditor() {
        dialog.close();
        auctionForm.setAuctionDto(null);
        itemForm.setItemDto(null);
        removeClassName("editing");
    }

    private ComponentRenderer<ActiveAuctionDetails, ActiveAuctionDTO> createAuctionDetailsRenderer() {
        return new ComponentRenderer<>(ActiveAuctionDetails::new);
    }

    private void configureDialog() {
        dialog.setHeaderTitle("New auction");
        var dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);
        add(dialog);
    }

    private VerticalLayout createDialogLayout() {
        var dialogLayout = new VerticalLayout(itemForm, auctionForm);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(Alignment.CENTER);
        dialogLayout.getStyle()
                .set("width", "48rem")
                .set("max-width", "100%");
        dialogLayout.add(new HorizontalLayout(saveAuctionButton, closeDialogButton));

        return dialogLayout;
    }

    private void configureItemForm() {
        auctionForm = new AuctionForm();
        auctionForm.setWidth("32em");
        auctionForm.setVisible(true);
    }

    private void configureAuctionForm() {
        itemForm = new ItemForm(itemCategoryService.findAllCategories());
        itemForm.setWidth("32em");
        itemForm.setVisible(true);
    }

    private void configureActionButtons() {
        saveAuctionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveAuctionButton.addClickListener(event -> {
            System.out.println(itemForm.getItemCategorySelect().getValue());
            System.out.println(auctionForm.getExpirationDatePicker().getValue());
        });

        closeDialogButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeDialogButton.addClickListener(click -> closeEditor());
    }

}
