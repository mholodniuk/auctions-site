package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.FinishedAuctionDTO;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.security.SecurityService;
import com.pwr.auctionsite.security.model.CustomUser;
import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;

@PermitAll
@Route(value = "archive", layout = MainLayout.class)
@PageTitle("Archive")
public class ArchivedAuctionsListView extends VerticalLayout {
    Grid<FinishedAuctionDTO> grid = new Grid<>(FinishedAuctionDTO.class, false);
    private final Checkbox showMineOnly = new Checkbox("Show mine only");
    private final AuctionService auctionService;
    private final CustomUser currentUser;

    public ArchivedAuctionsListView(@Autowired AuctionService auctionService,
                                    @Autowired SecurityService securityService) {
        this.auctionService = auctionService;
        currentUser = (CustomUser) securityService.getAuthenticatedUser();
        setSizeFull();
        addClassName("list-view");
        configureGrid();
        add(getToolbar(), getContent());
        updateList(currentUser.getId());
    }

    private void updateList(Long userId) {
        grid.setItems(query -> auctionService.findArchivedAuctions(userId, query.getOffset(), query.getLimit())
                .stream());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setPageSize(50);
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(FinishedAuctionDTO::name).setHeader("Item");
        grid.addColumn(FinishedAuctionDTO::category).setHeader("Category");
        grid.addColumn(FinishedAuctionDTO::buyer).setHeader("Winner");
        grid.addColumn(FinishedAuctionDTO::seller).setHeader("Seller");
        grid.addColumn(FinishedAuctionDTO::itemQuantity).setHeader("Item quantity").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(FinishedAuctionDTO::finalPrice).setHeader("Final price").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(FinishedAuctionDTO::seller).setHeader("Seller");
        grid.addColumn(auction -> auction.finishedAt().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")))
                .setHeader("Finished at");
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        showMineOnly.setValue(true);
        showMineOnly.addClickListener(e -> {
            if (showMineOnly.getValue()) {
                updateList(currentUser.getId());
            } else {
                updateList(null);
            }
        });

        var toolbar = new HorizontalLayout(showMineOnly);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}