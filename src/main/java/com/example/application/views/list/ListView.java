package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CRMService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

// Fajne komponenty:
// - Grid -> do wyświetlenia listy rzeczy
// - Grid/Item-details -> do wyświetlenia więcej informacji o rzeczy
// - Grid/Lazy-loading -> do paginacji między stronami w liście
// - Grid/Filters-Sorting
// - Grid/Buffered -> do edytowania wierszy inline
// - Icons
//
//
//



@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {
    // https://vaadin.com/docs/latest/binding-data/data-provider lazy loading danych
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form;
    Dialog dialog = new Dialog();
    CRMService service;

    public ListView(CRMService service) {
        this.service = service;
        addClassName("list-view");
        configureForm();
        configureDialog();
        setSizeFull();
        configureGrid();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureDialog() {
        dialog.setHeaderTitle("New employee");
        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);
        add(dialog);
    }

    private VerticalLayout createDialogLayout() {
        VerticalLayout dialogLayout = new VerticalLayout(form);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "32rem").set("max-width", "100%");

        return dialogLayout;
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("32em");
        form.setVisible(true);
        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            addClassName("editing");
            dialog.open();
        }
    }

    private void closeEditor() {
        dialog.close();
        form.setContact(null);
        removeClassName("editing");
    }
}