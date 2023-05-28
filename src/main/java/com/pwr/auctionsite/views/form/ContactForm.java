package com.pwr.auctionsite.views.form;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

public class ContactForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
//    ComboBox<Status> status = new ComboBox<>("Status");
//    ComboBox<Company> company = new ComboBox<>("Company");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    private String currentUrl = "";

//    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    public ContactForm() {
        addClassName("contact-form");
//        binder.bindInstanceFields(this);
//        company.setItems(companies);
//        company.setItemLabelGenerator(Company::getName);
//        status.setItems(statuses);
//        status.setItemLabelGenerator(Status::getName);

        add(firstName, lastName, email, createButtonsLayout());
    }

//    public void setContact(Contact contact) {
//        binder.setBean(contact);
//    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> System.out.println("save"));
        delete.addClickListener(event -> System.out.println("delete"));
        close.addClickListener(event -> System.out.println("close"));

//        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

//    private void validateAndSave() {
//        if (binder.isValid()) {
//            fireEvent(new SaveEvent(this, binder.getBean()));
//        }
//    }

//    // Events
//    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
//        private final Contact contact;
//
//        protected ContactFormEvent(ContactForm source, Contact contact) {
//            super(source, false);
//            this.contact = contact;
//        }
//
//        public Contact getContact() {
//            return contact;
//        }
//    }
//
//    public static class SaveEvent extends ContactFormEvent {
//        SaveEvent(ContactForm source, Contact contact) {
//            super(source, contact);
//        }
//    }
//
//    public static class DeleteEvent extends ContactFormEvent {
//        DeleteEvent(ContactForm source, Contact contact) {
//            super(source, contact);
//        }
//    }
//
//    public static class CloseEvent extends ContactFormEvent {
//        CloseEvent(ContactForm source) {
//            super(source, null);
//        }
//    }
//
//    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
//        return addListener(DeleteEvent.class, listener);
//    }
//
//    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
//        return addListener(SaveEvent.class, listener);
//    }
//
//    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
//        return addListener(CloseEvent.class, listener);
//    }
}