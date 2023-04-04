package com.example.application.views.main.frontend;

import com.example.application.views.main.data.SupportProfile;
import com.example.application.views.main.service.SupportProfileService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("support-profiles")
public class SupportProfilesView extends VerticalLayout {

    private Grid<SupportProfile> grid = new Grid<>(SupportProfile.class, false);
    private SupportProfileForm form = new SupportProfileForm();

    public SupportProfilesView(SupportProfileService supportProfileService) {
        grid.removeAllColumns();
        grid.setColumns("name" );
        grid.asSingleSelect().addValueChangeListener(event ->
        form.setSupportProfile(supportProfileService.findById(event.getValue().getId())));

        grid.setItems(supportProfileService.findAll());

        add(grid, form);
    }

}
