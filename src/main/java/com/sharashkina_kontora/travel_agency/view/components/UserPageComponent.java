package com.sharashkina_kontora.travel_agency.view.components;

import com.sharashkina_kontora.travel_agency.domain.User;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
public class UserPageComponent extends VerticalLayout {
    public UserPageComponent() {
    }

    public void initComponent(User user) {
        user.getOrders().forEach(order -> add(new OrderComponent(order)));
    }
}
