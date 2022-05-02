package com.sharashkina_kontora.travel_agency.view.components;

import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.view.components.tour.ShowAllToursComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
public class MainPageComponent {
    private final ShowAllToursComponent showAllToursComponent;

    public MainPageComponent(ShowAllToursComponent showAllToursComponent) {
        this.showAllToursComponent = showAllToursComponent;
    }

    public Component initComponent(User user) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(showAllToursComponent.initComponent(user));
        return verticalLayout;
    }
}
