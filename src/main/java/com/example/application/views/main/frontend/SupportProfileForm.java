package com.example.application.views.main.frontend;

import com.example.application.views.main.data.Contact;
import com.example.application.views.main.data.Impact;
import com.example.application.views.main.data.SupportProfile;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class SupportProfileForm extends VerticalLayout {

        private TextField name = new TextField("Name");
        private TextField ownerName = new TextField("Owner Name");
        private TextField ownerAddress = new TextField("Owner Address");
        private TextField ownerPhone = new TextField("Owner Phone");
        private TextField ownerType = new TextField("Owner Type");

        private Binder<SupportProfile> binder = new Binder<>(SupportProfile.class);

        private Grid<Impact> impactsGrid = new Grid<>(Impact.class);
        private Grid<Contact> consumersGrid = new Grid<>(Contact.class);

        public SupportProfileForm() {
                add(new H1("Support Profile"));
                FormLayout formLayout = new FormLayout();
                formLayout.add(name, ownerName, ownerAddress, ownerPhone, ownerType);
                add(formLayout);

                impactsGrid.setColumns("name", "severity", "thresholdPhrase");
                add(new H2("Impacts"), impactsGrid);

                consumersGrid.setColumns("name", "address", "phone", "type");
                add(new H2("Consumers"), consumersGrid);

                binder.bind(name, SupportProfile::getName, SupportProfile::setName);
                binder.forField(ownerName).bind(response -> response.getOwner().getName(),
                                (response, value) -> response.getOwner().setName(value));
                binder.forField(ownerAddress).bind(response -> response.getOwner().getAddress(),
                                (response, value) -> response.getOwner().setAddress(value));
                binder.forField(ownerPhone).bind(response -> response.getOwner().getPhone(),
                                (response, value) -> response.getOwner().setPhone(value));
                binder.forField(ownerType).bind(response -> response.getOwner().getType(),
                                (response, value) -> response.getOwner().setType(value));
        }

        public void setSupportProfile(SupportProfile supportProfile) {
                binder.setBean(supportProfile);
                impactsGrid.setItems(supportProfile.getImpacts());
                consumersGrid.setItems(supportProfile.getConsumers());
        }
}
