package com.example.application.views.main;

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

    private Binder<SupportProfileResponse> binder = new Binder<>(SupportProfileResponse.class);

    private Grid<SupportProfileResponse.Impact> impactsGrid = new Grid<>(SupportProfileResponse.Impact.class);
    private Grid<SupportProfileResponse.Consumer> consumersGrid = new Grid<>(SupportProfileResponse.Consumer.class);

    public SupportProfileForm() {
        add(new H1("Support Profile"));
        FormLayout formLayout = new FormLayout();
        formLayout.add(name, ownerName, ownerAddress, ownerPhone, ownerType);
        add(formLayout);

        impactsGrid.setColumns("name", "severity", "thresholdPhrase");
        add(new H2("Impacts"), impactsGrid);

        consumersGrid.setColumns("contact.name", "contact.address", "contact.phone", "contact.type");
        add(new H2("Consumers"), consumersGrid);

        binder.bind(name, SupportProfileResponse::getName, SupportProfileResponse::setName);
        binder.forField(ownerName).bind(response -> response.getOwner().getContact().getName(),
                (response, value) -> response.getOwner().getContact().setName(value));
        binder.forField(ownerAddress).bind(response -> response.getOwner().getContact().getAddress(),
                (response, value) -> response.getOwner().getContact().setAddress(value));
        binder.forField(ownerPhone).bind(response -> response.getOwner().getContact().getPhone(),
                (response, value) -> response.getOwner().getContact().setPhone(value));
        binder.forField(ownerType).bind(response -> response.getOwner().getContact().getType(),
                (response, value) -> response.getOwner().getContact().setType(value));
    }

    public void setSupportProfileResponse(SupportProfileResponse response) {
        binder.setBean(response);
        impactsGrid.setItems(response.getImpacts());
        consumersGrid.setItems(response.getConsumers());
    }
}
