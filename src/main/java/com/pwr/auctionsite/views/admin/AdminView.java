package com.pwr.auctionsite.views.admin;


import com.pwr.auctionsite.data.dto.views.UserInfoDTO;
import com.pwr.auctionsite.data.service.ItemCategoryService;
import com.pwr.auctionsite.data.service.UserService;
import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Admin panel")
@Route(value = "admin", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminView extends HorizontalLayout {
    private Tab manageUsersTab;
    private Tab manageCategoriesTab;
    private TabSheet tabs;
    Grid<UserInfoDTO> grid = new Grid<>(UserInfoDTO.class, false);
    TextField usernameFilter = new TextField();
    private final ListBox<String> listBox = new ListBox<>();
    private final TextField newCategoryField = new TextField("New category");
    private final ItemCategoryService itemCategoryService;
    private final UserService userService;

    public AdminView(UserService userService, ItemCategoryService itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
        this.userService = userService;
        setSizeFull();
        updateList();
        updateCategories();

        createTabs();

        configureGrid();
        configureCategories();
    }

    private void updateList() {
        grid.setItems(query -> userService.findUsersPaged(usernameFilter.getValue(), query.getOffset(), query.getLimit())
                .stream());
    }

    private void updateCategories() {
        listBox.setItems(itemCategoryService.findAllCategories());
    }

    private void createTabs() {
        manageUsersTab = new Tab("Manage users");
        manageCategoriesTab = new Tab("Manage categories");
        tabs = new TabSheet();
        tabs.setSizeFull();

        for (var tab : new Tab[]{manageUsersTab, manageCategoriesTab}) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        tabs.add(manageUsersTab, createUsersLayout());
        tabs.add(manageCategoriesTab, createCategoryLayout());
        add(tabs);
    }

    private VerticalLayout createCategoryLayout() {
        var categoriesLayout = new VerticalLayout();
        categoriesLayout.add(listBox);
        categoriesLayout.add(newCategoryField);
        return categoriesLayout;
    }

    private VerticalLayout createUsersLayout() {
        var userList = new VerticalLayout();
        userList.setSizeFull();
        userList.add(getToolbar(), getContent());
        return userList;
    }

    private void configureCategories() {
        newCategoryField.addValueChangeListener(value -> {
            itemCategoryService.addCategory(value.getValue());
            updateCategories();
        });
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setPageSize(50);
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(UserInfoDTO::id).setHeader("ID");
        grid.addColumn(UserInfoDTO::username).setHeader("Username");
        grid.addColumn(UserInfoDTO::fullName).setHeader("Full name");
        grid.addColumn(UserInfoDTO::email).setHeader("E-mail");
        grid.addColumn(AdminView::formatAddress).setHeader("Address");
        grid.addColumn(createStatusComponentRenderer()).setHeader("Status").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(this::createBlockButton).setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(this::createDeleteButton).setTextAlign(ColumnTextAlign.CENTER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private static String formatAddress(UserInfoDTO user) {
        return String.format("%s, %s, %s %s", user.country(),
                user.city(), user.street(), user.streetNumber());
    }

    private static final SerializableBiConsumer<Span, UserInfoDTO> statusComponentUpdater = (span, user) -> {
        String theme = String.format("badge %s", user.isBlocked() ? "error" : "success");
        span.getElement().setAttribute("theme", theme);
        span.setText(user.isBlocked() ? "BLOCKED" : "VALID");
    };

    private static ComponentRenderer<Span, UserInfoDTO> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private Button createBlockButton(UserInfoDTO user) {
        var actionButton = new Button(user.isBlocked() ? "enable" : "disable");
        actionButton.addThemeVariants(user.isBlocked() ? ButtonVariant.LUMO_SUCCESS : ButtonVariant.LUMO_ERROR);
        actionButton.addClickListener(event -> {
            userService.setUserBlocked(user.id(), !user.isBlocked());
            updateList();
        });
        return actionButton;
    }

    private Button createDeleteButton(UserInfoDTO user) {
        var actionButton = new Button("DELETE");
        actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        actionButton.addClickListener(event -> {
            userService.deleteById(user.id());
            updateList();
        });
        return actionButton;
    }

    private HorizontalLayout getContent() {
        var content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        usernameFilter.setPlaceholder("Search for...");
        usernameFilter.setClearButtonVisible(true);
        usernameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        usernameFilter.setClearButtonVisible(true);
        usernameFilter.addKeyPressListener(Key.ENTER, event -> updateList());
        Button queryButton = new Button("Search");
        queryButton.addClickListener(e -> updateList());

        var toolbar = new HorizontalLayout(usernameFilter, queryButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}