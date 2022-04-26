package com.sharashkina_kontora.travel_agency.view.components;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Status;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.view.components.order.EditOrderComponent;
import com.sharashkina_kontora.travel_agency.view.components.order.ShowAllUserOrdersComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringComponent
@UIScope
public class UserPageComponent extends VerticalLayout {
    private final ShowAllUserOrdersComponent showOrderComponent;
    private final EditOrderComponent editOrderComponent;
    private final Dialog dialog = new Dialog();

    private final Button add = new Button(VaadinIcon.PLUS.create());

    public UserPageComponent(ShowAllUserOrdersComponent showOrderComponent, EditOrderComponent editOrderComponent) {
        this.showOrderComponent = showOrderComponent;
        this.editOrderComponent = editOrderComponent;
    }

    public void initComponent(User user) {
        removeAll();
        add(add);
        add.addClickListener(menuItemClickEvent -> editOrderComponent.editOrder(Order.builder().user(user).status(Status.PLANNED).build()));
        user.getOrders().forEach(order -> add(showOrderComponent.initComponent(order)));
        if (user.getOrders().isEmpty())
            add(new H4("No orders yet..."));
        dialog.open();
        dialog.add(this);
    }
}
