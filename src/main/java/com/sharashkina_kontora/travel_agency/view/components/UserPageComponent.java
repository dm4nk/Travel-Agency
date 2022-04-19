package com.sharashkina_kontora.travel_agency.view.components;

import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.view.components.order.ShowAllUserOrdersComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
public class UserPageComponent extends VerticalLayout {
    private final ShowAllUserOrdersComponent showOrderComponent;
    public UserPageComponent(ShowAllUserOrdersComponent showOrderComponent) {

        this.showOrderComponent = showOrderComponent;
    }

    public void initComponent(User user) {
        user.getOrders().forEach(order -> add(showOrderComponent.initComponent(order)));
    }

    public void refreshComponent(User user){
        log.debug("Refreshing content of User Page");
        removeAll();
        initComponent(user);
    }
}
