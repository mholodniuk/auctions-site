package com.pwr.auctionsite.views.auctions;

import com.pwr.auctionsite.data.dto.FinishedAuctionDTO;
import com.pwr.auctionsite.data.service.AuctionService;
import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.format.DateTimeFormatter;

@PermitAll
@Route(value = "archive", layout = MainLayout.class)
@PageTitle("Archive")
public class ArchivedAuctionsListView extends VerticalLayout {
    Grid<FinishedAuctionDTO> grid = new Grid<>(FinishedAuctionDTO.class, false);
    private final AuctionService auctionService;

    public ArchivedAuctionsListView(AuctionService auctionService) {
        this.auctionService = auctionService;
        setSizeFull();
        addClassName("list-view");
        configureGrid();
        add(getContent());
        updateList();
    }

    private void updateList() {
        grid.setItems(query -> auctionService.findArchivedAuctions(query.getOffset(), query.getLimit()).stream());
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
        grid.addColumn(FinishedAuctionDTO::finalPrice).setHeader("Final price").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(auction -> auction.finishedAt().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")))
                .setHeader("Finished at");
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }
}