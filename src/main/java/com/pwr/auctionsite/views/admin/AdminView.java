package com.pwr.auctionsite.views.admin;


import com.pwr.auctionsite.data.service.ItemCategoryService;
import com.pwr.auctionsite.views.MainLayout;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin panel")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {
    private final ListBox<String> listBox = new ListBox<>();
    private final TextField newCategoryField = new TextField("New category");
    private final ItemCategoryService itemCategoryService;

    public AdminView(@Autowired ItemCategoryService itemCategoryService) {
        this.itemCategoryService = itemCategoryService;
        setSizeFull();
        updateCategories();
        add(listBox);
        newCategoryField.addValueChangeListener(value -> {
            itemCategoryService.addCategory(value.getValue());
            updateCategories();
        });
        add(newCategoryField);
    }

    void updateCategories() {
        listBox.setItems(itemCategoryService.findAllCategories());
    }
}
